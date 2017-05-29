package com.applexis.aimos.controller;

import com.applexis.aimos.model.*;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.entity.File;
import com.applexis.aimos.model.service.*;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.utils.StringUtils;
import com.applexis.utils.crypto.AESCrypto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StorageController {

    private final FileService fileService;
    private final DirectoryService directoryService;
    private final UserService userService;
    private final UserTokenService userTokenService;
    private final AccessModeService accessModeService;

    @Autowired
    public StorageController(FileService fileService,
                             DirectoryService directoryService,
                             UserService userService,
                             UserTokenService userTokenService,
                             AccessModeService accessModeService) {
        this.fileService = fileService;
        this.directoryService = directoryService;
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.accessModeService = accessModeService;
    }

    @RequestMapping(value = "/mobile-api/sync", method = RequestMethod.POST)
    @ResponseBody
    public SyncResponse syncStorage(@RequestParam(required = false) String fileDataList,
                                    @RequestParam String eToken,
                                    @RequestParam String base64PublicKey) {
        SyncResponse response;
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                List<FileData> fileData = null;
                if (fileDataList == null) {
                    fileData = new ArrayList<>();
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    fileDataList = fileDataList.replace("%7B", "{").replace("%7D", "}").replace("%22", "\"");
                    try {
                        fileData = objectMapper.readValue(fileDataList,
                                objectMapper.getTypeFactory().constructCollectionType(List.class, FileData.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileData != null) {
                    User user = userToken.getUser();
                    List<FSItem> dbFS = userService.getAllFiles(user);
                    List<FileData> result = new ArrayList<>();

                    checkHash(0, 0L, dbFS, fileData, result, aes, userToken.getUser());
                    for (FSItem item : dbFS) {
                        result.add(new FileData(item, FileData.DOWNLOAD, aes));
                    }

                    response = new SyncResponse(result, aes);
                } else {
                    response = new SyncResponse(SyncResponse.ErrorType.BAD_FILE_DATA.name(), aes);
                }
            } else {
                response = new SyncResponse(SyncResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new SyncResponse(SyncResponse.BAD_PUBLIC_KEY, aes);
        }
        return response;
    }

    private void checkHash(int treeParent, Long dbParentId, List<FSItem> dbFS,
                           List<FileData> fileData, List<FileData> result,
                           AESCrypto aes, User user) {
        List<Integer> treePart = fromParentId(fileData, treeParent, aes);
        List<FSItem> fsItems = fromParentId(dbFS, dbParentId);
        for (int i = 0; i < treePart.size(); i++) {
            FileData item = fileData.get(treePart.get(i));
            boolean found = findConformity(dbFS, fileData, result, aes, user, fsItems, treePart.get(i), item);
            if (!found) {
                registerOnServer(dbParentId, dbFS, fileData, result, aes, user, treePart.get(i), item);
            }
        }
    }

    private boolean findConformity(List<FSItem> dbFS, List<FileData> fileData,
                                   List<FileData> result, AESCrypto aes,
                                   User user, List<FSItem> fsItems, int i,
                                   FileData item) {
        int c = 0;
        boolean found = false;
        while (c < fsItems.size() && !found) {
            FSItem fsItem = fsItems.get(c);
            String name = fsItem instanceof File ?
                    ((File) fsItem).getName() :
                    ((Directory) fsItem).getName();
            boolean isFolder = fsItem instanceof Directory;
            if (item.getName(aes).equals(name) && item.getIsFolder(aes) == isFolder) {
                found = true;
                String hash = fsItem instanceof File ?
                        ((File) fsItem).getHash() :
                        ((Directory) fsItem).getHash();
                Long parentId = fsItem instanceof File ?
                        ((File) fsItem).getParentDirectory().getId() :
                        ((Directory) fsItem).getParentId();
                if (item.getHash(aes).equals(hash)) {
                    result.add(new FileData(fsItem, FileData.OK, aes));
                    dbFS.remove(fsItem);
                    if (fsItem instanceof Directory) {
                        Long fsId = ((Directory) fsItem).getParentId();
                        checkHash(i, fsId, dbFS, fileData, result, aes, user);
                    }
                } else {
                    if (fsItem instanceof Directory) {
                        Long fsId = ((Directory) fsItem).getId();
                        checkHash(i, fsId, dbFS, fileData, result, aes, user);
                    } else {
                        if (item.getLastModificationDatetime(aes).after(((File) fsItem).getLastModifiedDatetime())) {
                            result.add(new FileData(fsItem, FileData.UPLOAD, aes));
                            dbFS.remove(fsItem);
                        } else {
                            result.add(new FileData(fsItem, FileData.DOWNLOAD, aes));
                            dbFS.remove(fsItem);
                        }
                    }
                }
            }
            c++;
        }
        return found;
    }

    private void registerOnServer(Long dbParentId, List<FSItem> dbFS,
                                  List<FileData> fileData, List<FileData> result,
                                  AESCrypto aes, User user, int i, FileData item) {
        AccessMode mode = accessModeService.findByAccessMode(AccessMode.MODE_PRIVATE);
        if (item.getIsFolder(aes)) {
            Directory d = directoryService.create(
                    new Directory(item.getName(aes), dbParentId, user, mode, item.getHash(aes)));
            List<FSItem> tmp = userService.getAllFiles(user);
            try {
                Files.createDirectories(
                        Paths.get("./" + user.getLogin() + "/" +
                                getPath(tmp, dbParentId) + "/" +
                                item.getName(aes)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.add(new FileData(d, FileData.UPLOAD, aes));
            checkHash(i, d.getId(), dbFS, fileData, result, aes, user);
        } else {
            Directory d = directoryService.getById(dbParentId);
            File f = fileService.create(
                    new File(getPath(fileData, item, aes),
                            item.getName(aes), "", item.getSize(aes),
                            new Date(), item.getLastModificationDatetime(aes),
                            user, mode, d, ""
                    ));
            result.add(new FileData(f, FileData.UPLOAD, aes));
        }
    }

    private String getPath(List<FSItem> dbFS, Long dbParentId) {
        String result = "";
        FSItem tmp = getParent(dbFS, dbParentId);
        if (tmp != null) {
            while (tmp != null && ((Directory) tmp).getParentId() != 0) {
                result = ((Directory) tmp).getName() + "/" + result;
                tmp = getParent(dbFS, ((Directory) tmp).getParentId());
            }
            return tmp != null ?
                    ((Directory) tmp).getName() + "/" + result : result;
        }
        return "";
    }

    private String getPath(List<FileData> fileList, FileData file, AESCrypto aes) {
        String result = "";
        FileData tmp = fileList.get(file.getTreeParent(aes));
        while (tmp != null && tmp.getTreeParent(aes) != 0 && tmp.getTreeParent(aes) != -1) {
            result = tmp.getName(aes) + "/" + result;
            tmp = fileList.get(tmp.getTreeParent(aes));
        }
        return tmp != null && tmp.getTreeParent(aes) != -1 ? tmp.getName(aes) + "/" + result : result;
    }

    private FSItem getParent(List<FSItem> dbFS, Long dbParentId) {
        for (FSItem item : dbFS) {
            if (item instanceof Directory) {
                if (((Directory) item).getId().equals(dbParentId)) {
                    return item;
                }
            }
        }
        return null;
    }

    private List<Integer> fromParentId(List<FileData> list, int parentId, AESCrypto aes) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTreeParent(aes) == parentId) {
                result.add(i);
            }
        }
        return result;
    }

    private List<FSItem> fromParentId(List<FSItem> list, Long parentId) {
        return parentId == null ? new ArrayList<>() :
                list.stream()
                        .filter(item -> {
                            Long id = item instanceof Directory ?
                                    ((Directory) item).getParentId() :
                                    ((File) item).getParentDirectory().getId();
                            return id.equals(parentId);
                        })
                        .collect(Collectors.toList());
    }

    @RequestMapping(value = "/mobile-api/syncUpload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadResponse syncUpload(@RequestParam FileData eFileData,
                                         @RequestParam String eFilePath,
                                         @RequestParam String eKey,
                                         @RequestParam String eToken,
                                         @RequestParam String base64PublicKey,
                                         MultipartHttpServletRequest request) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        FileUploadResponse response = new FileUploadResponse(aes);
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String key = aes.decrypt(eKey);
            String filePath = aes.decrypt(eFilePath);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                File fInfo = fileService.getByPath(filePath, eFileData.getName(aes), userToken.getUser());
                response = updateKeyAndUpload(request, aes, response, key, filePath, fInfo);
            } else {
                response = new FileUploadResponse(FileUploadResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new FileUploadResponse(FileUploadResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/syncDownload", method = RequestMethod.POST)
    public void getFile(@RequestParam("eFilePath") String eFilePath,
                        @RequestParam String eToken,
                        @RequestParam String base64PublicKey,
                        HttpServletResponse response) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String filePath = aes.decrypt(eFilePath);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                String path = filePath.substring(0, filePath.lastIndexOf("/"));
                String name = filePath.substring(filePath.lastIndexOf("/") + 1);
                File fInfo = fileService.getByPath(path, name, userToken.getUser());
                if (fInfo != null) {
                    try {
                        java.io.File f = new java.io.File("./" + filePath);
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        InputStream is = new FileInputStream(f);
                        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                        response.flushBuffer();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @RequestMapping(value = "/mobile-api/singleUpload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadResponse singleFileUpload(@RequestParam("eName") String eName,
                                               @RequestParam("eSize") String eSize,
                                               @RequestParam("eHash") String eHash,
                                               @RequestParam("eParentDir") String eParentDir,
                                               @RequestParam("eKey") String eKey,
                                               @RequestParam("eToken") String eToken,
                                               @RequestParam("base64PublicKey") String base64PublicKey,
                                               MultipartHttpServletRequest request) throws IOException {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        FileUploadResponse response = new FileUploadResponse(aes);
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String key = aes.decrypt(eKey);
            String name = aes.decrypt(eName);
            String hash = aes.decrypt(eHash);
            Long parentDir = Long.valueOf(aes.decrypt(eParentDir));
            Long size = Long.valueOf(aes.decrypt(eSize));
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                String path = getPath(userService.getAllFiles(userToken.getUser()), parentDir);
                AccessMode mode = accessModeService.findByAccessMode(AccessMode.MODE_PRIVATE);
                Directory d = directoryService.getById(parentDir);
                File fInfo = fileService.create(new File(path, name, key, size,
                        new Date(), new Date(), userToken.getUser(), mode, d, hash));
                response = updateKeyAndUpload(request, aes, response, key,
                        "./" + userToken.getUser().getLogin() + "/" + path + "/" + name, fInfo);
            } else {
                response = new FileUploadResponse(FileUploadResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new FileUploadResponse(FileUploadResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    private FileUploadResponse updateKeyAndUpload(MultipartHttpServletRequest request,
                                                  AESCrypto aes, FileUploadResponse response,
                                                  String key, String filePath, File fInfo) {
        if (fInfo != null) {
            fInfo.setKey(key);
            fInfo = fileService.update(fInfo);
            if (fInfo != null) {
                saveFile(request, filePath, aes, response, fInfo);
            } else {
                response = new FileUploadResponse(FileUploadResponse.ErrorType.DATABASE_ERROR.name(), aes);
            }
        } else {
            response = new FileUploadResponse(FileUploadResponse.ErrorType.DATABASE_ERROR.name(), aes);
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/getFileKey", method = RequestMethod.POST)
    @ResponseBody
    public GetFileKeyResponse getFileKey(@RequestParam FileData eFileData,
                                         @RequestParam String eFilePath,
                                         @RequestParam String eToken,
                                         @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        GetFileKeyResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String filePath = aes.decrypt(eFilePath);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                File fInfo = fileService.getByPath(
                        filePath, eFileData.getName(aes), userToken.getUser());
                if (fInfo != null) {
                    response = new GetFileKeyResponse(fInfo.getKey(), aes, true);
                } else {
                    response = new GetFileKeyResponse(GetFileKeyResponse.ErrorType.DATABASE_ERROR.name(), aes);
                }
            } else {
                response = new GetFileKeyResponse(GetFileKeyResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new GetFileKeyResponse(GetFileKeyResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    private Directory getDirectoryByPath(String path, User user) {
        if (!path.equals("")) {
            List<Directory> userDirectories = directoryService.getUserRootDirectories(user);

            String[] pathParts = path.split("/");
            Directory newCurrent = null;
            for (String part : pathParts) {
                int c = 0;
                while (newCurrent == null || c < userDirectories.size()) {
                    if (userDirectories.get(c).getName().equals(part)) {
                        newCurrent = userDirectories.get(c);
                    }
                    c++;
                }
                if (newCurrent == null) {
                    return null;
                }
                userDirectories = directoryService.getChildDirectories(newCurrent.getId(), user);
            }
            return newCurrent;
        } else {
            return directoryService.getById(0L);
        }
    }

    private void saveFile(MultipartHttpServletRequest request,
                          String filePath, AESCrypto aes,
                          FileUploadResponse response, File fInfo) {
        try {
            String base64FileString = request.getParameter("file");
            java.io.File file = new java.io.File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            int i = 0;
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (i < base64FileString.length()) {
                byte[] tmp = StringUtils.fromHexString(
                        base64FileString.substring(i,
                                i + 1024 < base64FileString.length() ?
                                        i + 1024 :
                                        base64FileString.length()));
                i += 1024;
                fileOutputStream.write(tmp, 0, tmp.length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            response = new FileUploadResponse(new FileData(fInfo, FileData.OK, aes), aes);
        } catch (Exception ex) {
            response = new FileUploadResponse(FileUploadResponse.ErrorType.FILE_SAVE_ERROR.name(), aes);
        }
    }

    @RequestMapping(value = "/mobile-api/createFolder", method = RequestMethod.POST)
    @ResponseBody
    public FolderCreateResponse createFolder(@RequestParam String eFolderName,
                                             @RequestParam String eFolderPath,
                                             @RequestParam String eHash,
                                             @RequestParam String eToken,
                                             @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        FolderCreateResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String folderPath = aes.decrypt(eFolderPath);
            String folderName = aes.decrypt(eFolderName);
            String hash = aes.decrypt(eHash);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                AccessMode mode = accessModeService.findByAccessMode(AccessMode.MODE_PRIVATE);
                Directory d = getDirectoryByPath(folderPath, userToken.getUser());
                if (d != null) {
                    Directory dInfo = directoryService.create(new Directory(folderName, d.getId(),
                            userToken.getUser(), mode, hash));
                    if (dInfo != null) {
                        FileData fileData = new FileData(dInfo, FileData.OK, aes);
                        response = new FolderCreateResponse(aes, fileData);
                    } else {
                        response = new FolderCreateResponse(
                                FolderCreateResponse.ErrorType.DATABASE_ERROR.name(), aes);
                    }
                } else {
                    response = new FolderCreateResponse(
                            FolderCreateResponse.ErrorType.DATABASE_ERROR.name(), aes);
                }
            } else {
                response = new FolderCreateResponse(FolderCreateResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new FolderCreateResponse(FolderCreateResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

}

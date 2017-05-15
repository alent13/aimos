package com.applexis.aimos.controller;

import com.applexis.aimos.model.*;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.utils.crypto.AESCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public SyncResponse syncStorage(@RequestParam FileData[] fileData,
                                    @RequestParam String eToken,
                                    @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        SyncResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                User user = userToken.getUser();
                List<FSItem> dbFS = userService.getAllFiles(user);
                List<FileData> result = new ArrayList<>();

                checkHash(0, 0L, dbFS, fileData, result, aes, userToken.getUser());

                response = new SyncResponse(result, aes);
            } else {
                response = new SyncResponse(SyncResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new SyncResponse(SyncResponse.BAD_PUBLIC_KEY, aes);
        }
        return response;
    }

    private void checkHash(int treeParent, Long dbParentId, List<FSItem> dbFS,
                           FileData[] fileData, List<FileData> result,
                           AESCrypto aes, User user) {
        List<FileData> treePart = fromParentId(fileData, treeParent, aes);
        List<FSItem> fsItems = fromParentId(dbFS, dbParentId);
        for (int i = 0; i < treePart.size(); i++) {
            FileData item = treePart.get(i);
            boolean found = findConformity(dbFS, fileData, result, aes, user, fsItems, i, item);
            if (!found) {
                registerOnServer(dbParentId, dbFS, fileData, result, aes, user, i, item);
            }
        }
    }

    private boolean findConformity(List<FSItem> dbFS, FileData[] fileData,
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
            boolean folder = fsItem instanceof Directory;
            if (item.getName(aes).equals(name) && item.getIsFolder(aes) == folder) {
                found = true;
                String hash = fsItem instanceof File ?
                        ((File) fsItem).getHash() :
                        ((Directory) fsItem).getHash();
                if (item.getHash(aes).equals(hash)) {
                    List<FileData> subTreePart = fromParentId(fileData, i, aes);
                    subTreePart.forEach(fs -> result.add(new FileData(item, FileData.OK, aes)));
                } else {
                    addToSyncList(dbFS, fileData, result, aes, user, i, item, fsItem, folder);
                }
            }
            c++;
        }
        return found;
    }

    private void addToSyncList(List<FSItem> dbFS, FileData[] fileData,
                               List<FileData> result, AESCrypto aes, User user,
                               int i, FileData item, Object fsItem, boolean folder) {
        if (folder) {
            checkHash(i, ((Directory) fsItem).getId(), dbFS, fileData, result, aes, user);
        } else {
            if (item.getLastModificationDatetime(aes).after(((File) fsItem).getLastModifiedDatetime())) {
                result.add(new FileData(item, FileData.UPLOAD, aes));
            } else {
                result.add(new FileData(item, FileData.DOWNLOAD, aes));
            }
        }
    }

    private void registerOnServer(Long dbParentId, List<FSItem> dbFS,
                                  FileData[] fileData, List<FileData> result,
                                  AESCrypto aes, User user, int i, FileData item) {
        AccessMode mode = accessModeService.findByAccessMode(AccessMode.MODE_PRIVATE);
        if (item.getIsFolder(aes)) {
            Directory d = directoryService.create(
                    new Directory(item.getName(aes), dbParentId, user, mode, item.getHash(aes)));
            try {
                Files.createDirectories(
                        Paths.get("./" + user.getLogin() + "/" +
                                getPath(dbFS, dbParentId) + "/" +
                                item.getName(aes)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkHash(i, d.getId(), dbFS, fileData, result, aes, user);
        } else {
            Directory d = directoryService.getById(dbParentId);
            fileService.create(
                    new File(getPath(fileData, item, aes),
                            item.getName(aes), "", item.getSize(aes),
                            item.getCreateDatetime(aes),
                            item.getLastModificationDatetime(aes),
                            user, mode, d, item.getHash(aes)
                    ));
            result.add(new FileData(item, FileData.UPLOAD, aes));
        }
    }

    private String getPath(List<FSItem> dbFS, Long dbParentId) {
        FSItem tmp = getParent(dbFS, dbParentId);
        if (tmp != null) {
            String result = ((Directory) tmp).getName();
            while (tmp != null && ((Directory) tmp).getParentId() != 0) {
                result = ((Directory) tmp).getName() + "/" + result;
                tmp = getParent(dbFS, ((Directory) tmp).getParentId());
            }
            return tmp != null ?
                    ((Directory) tmp).getName() + "/" + result : result;
        }
        return null;
    }

    private String getPath(FileData[] list, FileData file, AESCrypto aes) {
        String result = "";
        List<FileData> fileList = Arrays.asList(list);
        int fileIndex = fileList.indexOf(file);
        FileData tmp = fileList.get(fileIndex);
        while (tmp.getTreeParent(aes) != 0) {
            result = tmp.getName(aes) + "/" + result;
            tmp = fileList.get(tmp.getTreeParent(aes));
        }
        return result;
    }

    private FSItem getParent(List<FSItem> dbFS, Long dbParentId) {
        for (FSItem item : dbFS) {
            if (((Directory) item).getId().equals(dbParentId)) {
                return item;
            }
        }
        return null;
    }

    private List<FileData> fromParentId(FileData[] list, int parentId, AESCrypto aes) {
        return Stream.of(list)
                .filter(item -> item.getTreeParent(aes) == parentId)
                .collect(Collectors.toList());
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
    public FileUploadResponse syncUpload(@RequestParam("file") MultipartFile file,
                                         @RequestParam FileData eFileData,
                                         @RequestParam String eFilePath,
                                         @RequestParam String eKey,
                                         @RequestParam String eToken,
                                         @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        FileUploadResponse response = new FileUploadResponse(aes);
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String key = aes.decrypt(eKey);
            String filePath = aes.decrypt(eFilePath);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                File fInfo = fileService.getByPath(filePath, eFileData.getName(aes), userToken.getUser());
                response = updateKeyAndUpload(file, aes, response, key, filePath, fInfo);
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
                        InputStream is = new FileInputStream(new java.io.File("./" + filePath));
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
    public FileUploadResponse singleFileUpload(@RequestParam("file") MultipartFile file,
                                               @RequestParam FileData eFileData,
                                               @RequestParam String eKey,
                                               @RequestParam String eFilePath,
                                               @RequestParam String eToken,
                                               @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        FileUploadResponse response = new FileUploadResponse(aes);
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String key = aes.decrypt(eKey);
            String filePath = aes.decrypt(eFilePath);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null && userToken.getUser() != null) {
                File fInfo = fileService.getByPath(
                        filePath, eFileData.getName(aes), userToken.getUser());
                if (fInfo == null) {
                    AccessMode mode = accessModeService.findByAccessMode(AccessMode.MODE_PRIVATE);
                    Directory d = getDirectoryByPath(filePath, userToken.getUser());
                    fInfo = fileService.create(new File(filePath, eFileData.getName(aes),
                            key, eFileData.getSize(aes), eFileData.getCreateDatetime(aes),
                            eFileData.getLastModificationDatetime(aes),
                            userToken.getUser(), mode, d, eFileData.getHash(aes)));
                }
                response = updateKeyAndUpload(file, aes, response, key, filePath, fInfo);
            } else {
                response = new FileUploadResponse(FileUploadResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new FileUploadResponse(FileUploadResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    private FileUploadResponse updateKeyAndUpload(@RequestParam("file") MultipartFile file, AESCrypto aes, FileUploadResponse response, String key, String filePath, File fInfo) {
        if (fInfo != null) {
            fInfo.setKey(key);
            fInfo = fileService.update(fInfo);
            if (fInfo != null) {
                response = tryUpload(file, aes, response, filePath, fInfo);
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

    private FileUploadResponse tryUpload(MultipartFile file, AESCrypto aes,
                                         FileUploadResponse response,
                                         String filePath, File fInfo) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("./" + filePath + file.getName());
            Files.write(path, bytes);
            response = new FileUploadResponse(new FileData(fInfo, FileData.OK, aes), aes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Directory getDirectoryByPath(String path, User user) {
        List<Directory> userDirectories = directoryService.getUserRootDirectories(user);

        String[] pathParts = path.split("/");
        Directory newCurrent = null;
        for (String part : pathParts) {
            int c = 0;
            while (newCurrent == null || c++ < userDirectories.size()) {
                if (userDirectories.get(c).getName().equals(part)) {
                    newCurrent = userDirectories.get(c);
                }
            }
            if (newCurrent == null) {
                return null;
            }
            userDirectories = directoryService.getChildDirectories(newCurrent.getId(), user);
        }
        return newCurrent;
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
                        response = new FolderCreateResponse(aes, new FileData(dInfo, FileData.OK, aes));
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

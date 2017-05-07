package com.applexis.aimos.controller;

import com.applexis.aimos.model.*;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.repository.StatusRepository;
import com.applexis.aimos.model.service.*;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.utils.crypto.AESCrypto;
import com.applexis.utils.crypto.DSASign;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DialogController {

    public final UserService userService;

    public final DialogService dialogService;

    public final DialogUserService dialogUserService;

    public final StatusRepository statusRepository;

    public final UserTokenService userTokenService;

    public final MessageService messageService;

    @Autowired
    public DialogController(UserService userService,
                            DialogService dialogService,
                            DialogUserService dialogUserService,
                            StatusRepository statusRepository,
                            UserTokenService userTokenService,
                            MessageService messageService) {
        this.userService = userService;
        this.dialogService = dialogService;
        this.dialogUserService = dialogUserService;
        this.statusRepository = statusRepository;
        this.userTokenService = userTokenService;
        this.messageService = messageService;
    }

    @RequestMapping(value = "/mobile-api/createGroup", method = RequestMethod.GET)
    @ResponseBody
    public DialogResponse createGroup(@RequestParam String eDialogName,
                                      @RequestParam String eToken,
                                      @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        DialogResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            String dialogName = aes.decrypt(eDialogName);
            UserToken userToken = userTokenService.getByToken(token);
            Dialog dialog = null;
            if (userToken != null) {
                dialog = new Dialog(dialogName);
                dialog = dialogService.createDialog(dialog);
                if (dialog != null) {
                    DialogUser dialogUser = new DialogUser(dialog, userToken.getUser(),
                            statusRepository.findByStatus(Status.ADMIN));
                    dialogUser = dialogUserService.create(dialogUser);
                    if (dialogUser != null) {
                        response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog), aes);
                    } else {
                        response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name(), aes);
                    }
                } else {
                    response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name(), aes);
                }
            } else {
                response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new DialogResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/createDialog", method = RequestMethod.POST)
    @ResponseBody
    public DialogResponse createDialog(@RequestParam String eIdUser,
                                       @RequestParam String eToken,
                                       @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        DialogResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            Long idUser = Long.parseLong(aes.decrypt(eIdUser));
            UserToken userToken = userTokenService.getByToken(token);
            Dialog dialog = null;
            if (userToken != null) {
                User user = userService.getById(idUser);
                if (user != null) {
                    dialog = dialogService.findByName(user.getName() + " " + user.getSurname());
                    if (dialog == null) {
                        dialog = new Dialog(user.getName() + " " + user.getSurname());
                        dialog = dialogService.createDialog(dialog);
                        if (dialog != null) {
                            DialogUser dialogUser = new DialogUser(dialog, userToken.getUser(),
                                    statusRepository.findByStatus(Status.ADMIN));
                            dialogUser = dialogUserService.create(dialogUser);
                            if(dialogUser == null) {
                                response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name(), aes);
                            }
                            dialogUser = new DialogUser(dialog, user,
                                    statusRepository.findByStatus(Status.MEMBER));
                            dialogUser = dialogUserService.create(dialogUser);
                            if (dialogUser != null) {
                                response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog), aes);
                            } else {
                                response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name(), aes);
                            }
                        } else {
                            response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name(), aes);
                        }
                    } else {
                        response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog), aes);
                    }
                } else {
                    response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_ID.name(), aes);
                }
            } else {
                response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new DialogResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/getDialogs", method = RequestMethod.POST)
    @ResponseBody
    public DialogListResponse getDialogs(@RequestParam String eToken,
                                         @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        DialogListResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                List<Dialog> dialogList = dialogUserService.getDialogByUser(userToken.getUser());
                if (dialogList != null) {
                    List<DialogMinimal> dialogMinimalList = new ArrayList<>();
                    for (Dialog dialog : dialogList) {
                        DialogMinimal minimal = new DialogMinimal();
                        minimal.setId(dialog.getId(), aes);
                        minimal.setName(dialog.getName(), aes);
                        Message lastMessage = messageService.lastMessageInDialog(dialog);
                        KeyPair keyPair = DSASign.generateKeyPair();
                        String signature = Base64.encodeBase64String(DSASign.generateSignature(keyPair.getPrivate(), lastMessage.getMessageText().getBytes()));
                        minimal.setLastMessage(new MessageMinimal(lastMessage, signature, DSASign.getPublicKeyString(keyPair.getPublic()), aes));
                        minimal.setLastSender(new UserMinimalInfo(lastMessage.getSender(), aes));
                        List<User> users = dialogUserService.getUserByDialog(dialog);
                        if (users != null) {
                            List<UserMinimalInfo> userMinimalInfoList = new ArrayList<>();
                            for (User user : users) {
                                userMinimalInfoList.add(new UserMinimalInfo(user, aes));
                            }
                            minimal.setUsers(userMinimalInfoList);
                        } else {
                            response = new DialogListResponse(DialogListResponse.ErrorType.DATABASE_ERROR.name(), aes);
                            break;
                        }
                        dialogMinimalList.add(minimal);
                    }
                    response = new DialogListResponse(dialogMinimalList, aes);
                } else {
                    response = new DialogListResponse(DialogListResponse.ErrorType.DATABASE_ERROR.name(), aes);
                }
            } else {
                response = new DialogListResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new DialogListResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

}

package com.applexis.aimos.controller;

import com.applexis.aimos.model.DialogListResponse;
import com.applexis.aimos.model.DialogMinimal;
import com.applexis.aimos.model.DialogResponse;
import com.applexis.aimos.model.UserMinimalInfo;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.repository.StatusRepository;
import com.applexis.aimos.model.service.DialogService;
import com.applexis.aimos.model.service.DialogUserService;
import com.applexis.aimos.model.service.UserService;
import com.applexis.aimos.model.service.UserTokenService;
import com.applexis.aimos.utils.DESCryptoHelper;
import com.applexis.aimos.utils.KeyExchangeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DialogController {

    public final UserService userService;

    public final DialogService dialogService;

    public final DialogUserService dialogUserService;

    public final StatusRepository statusRepository;

    public final UserTokenService userTokenService;

    @Autowired
    public DialogController(UserService userService,
                            DialogService dialogService,
                            DialogUserService dialogUserService,
                            StatusRepository statusRepository,
                            UserTokenService userTokenService) {
        this.userService = userService;
        this.dialogService = dialogService;
        this.dialogUserService = dialogUserService;
        this.statusRepository = statusRepository;
        this.userTokenService = userTokenService;
    }

    @RequestMapping(value = "/mobile-api/createGroup", method = RequestMethod.GET)
    @ResponseBody
    public DialogResponse createGroup(@RequestParam String eDialogName,
                                      @RequestParam String eToken,
                                      @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        DialogResponse response = new DialogResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            String dialogName = DESCryptoHelper.decrypt(DESKey, eDialogName);
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
                        response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog));
                    } else {
                        response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name());
                    }
                } else {
                    response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name());
                }
            } else {
                response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new DialogResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/createDialog", method = RequestMethod.POST)
    @ResponseBody
    public DialogResponse createDialog(@RequestParam String eIdUser,
                                       @RequestParam String eToken,
                                       @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        DialogResponse response = new DialogResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            Long idUser = Long.parseLong(DESCryptoHelper.decrypt(DESKey, eIdUser));
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
                                response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name());
                            }
                            dialogUser = new DialogUser(dialog, user,
                                    statusRepository.findByStatus(Status.MEMBER));
                            dialogUser = dialogUserService.create(dialogUser);
                            if (dialogUser != null) {
                                response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog));
                            } else {
                                response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name());
                            }
                        } else {
                            response = new DialogResponse(DialogResponse.ErrorType.DATABASE_ERROR.name());
                        }
                    } else {
                        response = new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog));
                    }
                } else {
                    response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_ID.name());
                }
            } else {
                response = new DialogResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new DialogResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/getDialogs", method = RequestMethod.POST)
    @ResponseBody
    public DialogListResponse getDialogs(@RequestParam String eToken,
                                         @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        DialogListResponse response = new DialogListResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                List<Dialog> dialogList = dialogUserService.getDialogByUser(userToken.getUser());
                if (dialogList != null) {
                    List<DialogMinimal> dialogMinimalList = new ArrayList<>();
                    for (Dialog dialog : dialogList) {
                        DialogMinimal minimal = new DialogMinimal();
                        minimal.setId(dialog.getId());
                        minimal.setName(dialog.getName());
                        List<User> users = dialogUserService.getUserByDialog(dialog);
                        if (users != null) {
                            List<UserMinimalInfo> userMinimalInfoList = new ArrayList<>();
                            for (User user : users) {
                                userMinimalInfoList.add(new UserMinimalInfo(user));
                            }
                            minimal.setUsers(userMinimalInfoList);
                        } else {
                            response = new DialogListResponse(DialogListResponse.ErrorType.DATABASE_ERROR.name());
                            break;
                        }
                        dialogMinimalList.add(minimal);
                    }
                    response = new DialogListResponse(dialogMinimalList);
                } else {
                    response = new DialogListResponse(DialogListResponse.ErrorType.DATABASE_ERROR.name());
                }
            } else {
                response = new DialogListResponse(DialogResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new DialogListResponse(DialogResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

}

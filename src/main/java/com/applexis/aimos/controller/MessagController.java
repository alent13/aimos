package com.applexis.aimos.controller;

import com.applexis.aimos.model.MessageResponse;
import com.applexis.aimos.model.MessageSendResponse;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
import com.applexis.aimos.utils.DESCryptoHelper;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.aimos.utils.RSACryptoHelper;
import com.applexis.aimos.utils.SHAHashHelper;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
import java.security.Principal;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class MessagController {

    private final UserService userService;

    private final UserTokenService userTokenService;

    private final DialogService dialogService;

    private final ContactListService contactListService;

    private final MessageService messageService;

    private final DialogUserService dialogUserService;

    private final NotificationService notificationService;

    private final NotificationTypeService notificationTypeService;

    @Autowired
    public MessagController(UserService userService, UserTokenService userTokenService, DialogService dialogService, ContactListService contactListService, MessageService messageService, DialogUserService dialogUserService, NotificationService notificationService, NotificationTypeService notificationTypeService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.dialogService = dialogService;
        this.contactListService = contactListService;
        this.messageService = messageService;
        this.dialogUserService = dialogUserService;
        this.notificationService = notificationService;
        this.notificationTypeService = notificationTypeService;
    }

    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String allMessages(Model model, Principal principal) {
        model.addAttribute("login", principal.getName());
        Long userId = userService.getByLogin(principal.getName()).getId();
        model.addAttribute("contacts", contactListService.findFriends(userId));
        return "mindex";
    }

    @RequestMapping(value = "/mobile-api/getLastMessages", method = RequestMethod.POST)
    @ResponseBody
    public MessageResponse getLastMessages(@RequestParam String eCount,
                                           @RequestParam String eOffset,
                                           @RequestParam String eIdDialog,
                                           @RequestParam String eToken,
                                           @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        MessageResponse response = new MessageResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            Long idDialog = Long.parseLong(DESCryptoHelper.decrypt(DESKey, eIdDialog));
            int count = Integer.parseInt(DESCryptoHelper.decrypt(DESKey, eCount));
            int offset = Integer.parseInt(DESCryptoHelper.decrypt(DESKey, eOffset));
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                Dialog dialog = dialogService.getDialog(idDialog);
                if (dialog != null && dialogUserService.getDialogByUser(userToken.getUser()).contains(dialog)) {
                    List<Message> messages = messageService.getLastMessages(dialog, offset, count);
                    if (messages != null) {
                        response = new MessageResponse(messages);
                    } else {
                        response = new MessageResponse(MessageResponse.ErrorType.DATABASE_ERROR.name());
                    }
                } else {
                    response = new MessageResponse(MessageResponse.ErrorType.INCORRECT_ID.name());
                }
            } else {
                response = new MessageResponse(MessageResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new MessageResponse(MessageResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/sendMessageEncrypted", method = RequestMethod.POST)
    @ResponseBody
    public MessageSendResponse sendMessageEncrypted(@RequestParam String eMessage,
                                                    @RequestParam String eKey,
                                                    @RequestParam String eEds,
                                                    @RequestParam String eEdsPublicKey,
                                                    @RequestParam String eIdDialog,
                                                    @RequestParam String eToken,
                                                    @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        MessageSendResponse response = new MessageSendResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            String messageText = DESCryptoHelper.decrypt(DESKey, eMessage);
            String key = DESCryptoHelper.decrypt(DESKey, eKey);
            String eds = DESCryptoHelper.decrypt(DESKey, eEds);
            String edsPublicKey = DESCryptoHelper.decrypt(DESKey, eEdsPublicKey);
            Long idDialog = Long.parseLong(DESCryptoHelper.decrypt(DESKey, eIdDialog));
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                Dialog dialog = dialogService.getDialog(idDialog);
                if (dialog != null && dialogUserService.getDialogByUser(userToken.getUser()).contains(dialog)) {
                    PublicKey edsPublic = RSACryptoHelper.getPublicKey(edsPublicKey);
                    String messageHash = Hex.encodeHexString(RSACryptoHelper.decrypt(edsPublic, Base64.decode(eds.getBytes())));
                    String calculatedHash = SHAHashHelper.getSHA512String(messageText, "message");
                    Message message = null;
                    if (Objects.equals(messageHash, calculatedHash)) {
                        message = new Message(userToken.getUser(),
                                dialog, key, messageText, new Date());
                        message = messageService.sendMessage(message);
                        if (message != null) {
                            List<User> users = dialogUserService.getUserByDialog(dialog);
                            NotificationType type = notificationTypeService.fingByType(NotificationType.Type.GET_MESSAGE.name());

                            Notification notification = null;
                            for (User user : users) {
                                notification = new Notification(user, type, dialog, userToken.getUser(), new Date());
                                notification = notificationService.create(notification);
                                if (notification == null) {
                                    response = new MessageSendResponse(MessageSendResponse.ErrorType.DATABASE_ERROR.name());
                                    break;
                                }
                            }

                            if (notification != null) {
                                response = new MessageSendResponse(message);
                            }
                        } else {
                            response = new MessageSendResponse(MessageSendResponse.ErrorType.DATABASE_ERROR.name());
                        }
                    } else {
                        response = new MessageSendResponse(MessageSendResponse.ErrorType.BAD_HASH.name());
                    }
                } else {
                    response = new MessageSendResponse(MessageSendResponse.ErrorType.INCORRECT_ID.name());
                }
            } else {
                response = new MessageSendResponse(MessageSendResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new MessageSendResponse(MessageSendResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public MessageSendResponse sendMessage(@RequestParam String eMessage,
                                           @RequestParam String eEds,
                                           @RequestParam String eEdsPublicKey,
                                           @RequestParam String eIdDialog,
                                           @RequestParam String eToken,
                                           @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        MessageSendResponse response = new MessageSendResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            String messageText = DESCryptoHelper.decrypt(DESKey, eMessage);
            String eds = DESCryptoHelper.decrypt(DESKey, eEds);
            String edsPublicKey = DESCryptoHelper.decrypt(DESKey, eEdsPublicKey);
            Long idDialog = Long.parseLong(DESCryptoHelper.decrypt(DESKey, eIdDialog));
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                Dialog dialog = dialogService.getDialog(idDialog);
                if (dialog != null && dialogUserService.getDialogByUser(userToken.getUser()).contains(dialog)) {
                    PublicKey edsPublic = RSACryptoHelper.getPublicKey(edsPublicKey);
                    String messageHash = Hex.encodeHexString(RSACryptoHelper.decrypt(edsPublic, Base64.decode(eds.getBytes())));
                    String calculatedHash = SHAHashHelper.getSHA512String(messageText, "message");
                    Message message = null;
                    if (Objects.equals(messageHash, calculatedHash)) {
                        message = new Message(userToken.getUser(),
                                dialog, "", messageText, new Date());
                        message = messageService.sendMessage(message);
                        if (message != null) {
                            List<User> users = dialogUserService.getUserByDialog(dialog);
                            NotificationType type = notificationTypeService.fingByType(NotificationType.Type.GET_MESSAGE.name());

                            Notification notification = null;
                            for (User user : users) {
                                notification = new Notification(user, type, dialog, userToken.getUser(), new Date());
                                notification = notificationService.create(notification);
                                if (notification == null) {
                                    response = new MessageSendResponse(MessageSendResponse.ErrorType.DATABASE_ERROR.name());
                                    break;
                                }
                            }

                            if (notification != null) {
                                response = new MessageSendResponse(message);
                            }
                        } else {
                            response = new MessageSendResponse(MessageSendResponse.ErrorType.DATABASE_ERROR.name());
                        }
                    } else {
                        response = new MessageSendResponse(MessageSendResponse.ErrorType.BAD_HASH.name());
                    }
                } else {
                    response = new MessageSendResponse(MessageSendResponse.ErrorType.INCORRECT_ID.name());
                }
            } else {
                response = new MessageSendResponse(MessageSendResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new MessageSendResponse(MessageSendResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

}

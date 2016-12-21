package com.applexis.aimos.controller;

import com.applexis.aimos.model.AddContactResponse;
import com.applexis.aimos.model.ContactResponse;
import com.applexis.aimos.model.UserMinimalInfo;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
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
import java.util.Date;
import java.util.List;

@Controller
public class ContactController {

    public final UserTokenService userTokenService;

    public final UserService userService;

    public final ContactListService contactListService;

    public final NotificationTypeService notificationTypeService;

    public final NotificationService notificationService;

    @Autowired
    public ContactController(UserTokenService userTokenService, UserService userService, ContactListService contactListService, NotificationTypeService notificationTypeService, NotificationService notificationService) {
        this.userTokenService = userTokenService;
        this.userService = userService;
        this.contactListService = contactListService;
        this.notificationTypeService = notificationTypeService;
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/mobile-api/findContact", method = RequestMethod.POST)
    @ResponseBody
    public ContactResponse findContact(@RequestParam String eLoginPart,
                                       @RequestParam String eToken,
                                       @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        ContactResponse response = new ContactResponse();
        if (DESKey != null) {
            String loginPart = DESCryptoHelper.decrypt(DESKey, eLoginPart);
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                List<User> usersLike = userService.getContacts(loginPart);
                List<User> contacts = contactListService.findFriends(userToken.getUser().getId());

                List<UserMinimalInfo> users = new ArrayList<>();

                for (User user : usersLike) {
                    if (user.getUserSettings().getPageShow().getMode().equals(ShowMode.EVERYONE)
                            && !contacts.contains(user)
                            && !user.getLogin().equals(userToken.getUser().getLogin())) {
                        users.add(new UserMinimalInfo(user));
                    }
                }
                response = new ContactResponse(users);
            } else {
                response = new ContactResponse(ContactResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new ContactResponse(ContactResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/addContact", method = RequestMethod.POST)
    @ResponseBody
    public AddContactResponse addContact(@RequestParam String eIdUser,
                                         @RequestParam String eToken,
                                         @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        AddContactResponse response = null;
        if (DESKey != null) {
            Long idUser = Long.parseLong(DESCryptoHelper.decrypt(DESKey, eIdUser));
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                User user = userService.getById(idUser);
                if (user != null) {
                    List<User> contacts = contactListService.findFriends(userToken.getUser().getId());
                    if(!contacts.contains(user)) {
                        ContactList newContact = new ContactList(userToken.getUser().getId(), user);
                        newContact = contactListService.save(newContact);
                        if (newContact != null) {
                            NotificationType type = notificationTypeService.fingByType(NotificationType.Type.ADDED_TO_FRIEND_LIST.name());

                            Notification notification = new Notification(user, type, userToken.getUser(), new Date());
                            notification = notificationService.create(notification);
                            if (notification == null) {
                                response = new AddContactResponse(AddContactResponse.ErrorType.DATABASE_ERROR.name());
                            } else {
                                response = new AddContactResponse(new UserMinimalInfo(user));
                            }
                        } else {
                            response = new AddContactResponse(AddContactResponse.ErrorType.ALREADY_FRIENDS.name());
                        }
                    } else {
                        response = new AddContactResponse(AddContactResponse.ErrorType.DATABASE_ERROR.name());
                    }
                } else {
                    response = new AddContactResponse(AddContactResponse.ErrorType.INCORRECT_ID.name());
                }
            } else {
                response = new AddContactResponse(AddContactResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new AddContactResponse(AddContactResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/getContacts", method = RequestMethod.POST)
    @ResponseBody
    public ContactResponse getContacts(@RequestParam String eToken,
                                       @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        ContactResponse response = new ContactResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                List<User> contacts = contactListService.findFriends(userToken.getUser().getId());

                List<UserMinimalInfo> users = new ArrayList<>();

                for (User user : contacts) {
                    users.add(new UserMinimalInfo(user));
                }

                response = new ContactResponse(users);
            } else {
                response = new ContactResponse(ContactResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new ContactResponse(ContactResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

}

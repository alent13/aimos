package com.applexis.aimos.controller;

import com.applexis.aimos.model.NotificationResponse;
import com.applexis.aimos.model.entity.Notification;
import com.applexis.aimos.model.entity.UserToken;
import com.applexis.aimos.model.service.NotificationService;
import com.applexis.aimos.model.service.UserTokenService;
import com.applexis.aimos.utils.DESCryptoHelper;
import com.applexis.aimos.utils.KeyExchangeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Key;
import java.util.List;

@Controller
public class NotificationController {

    private final UserTokenService userTokenService;

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserTokenService userTokenService, NotificationService notificationService) {
        this.userTokenService = userTokenService;
        this.notificationService = notificationService;
    }

    @RequestMapping(value = "/mobile-api/getNotifications", method = RequestMethod.POST)
    public NotificationResponse getNotifications(@RequestParam String eToken,
                                                 @RequestParam String base64PublicKey) {
        NotificationResponse response = new NotificationResponse();
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                List<Notification> notifications = notificationService.fingByUser(userToken.getUser());
                if (notifications == null) {
                    response = new NotificationResponse(NotificationResponse.ErrorType.DATABASE_ERROR.name());
                } else {
                    response = new NotificationResponse(notifications);
                }
            } else {
                response = new NotificationResponse(NotificationResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new NotificationResponse(NotificationResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }
}

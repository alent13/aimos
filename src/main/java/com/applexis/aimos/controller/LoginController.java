package com.applexis.aimos.controller;

import com.applexis.aimos.model.LoginResponse;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.entity.UserToken;
import com.applexis.aimos.model.service.AuthenticationService;
import com.applexis.aimos.model.service.UserService;
import com.applexis.aimos.model.service.UserTokenService;
import com.applexis.aimos.utils.DESCryptoHelper;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.aimos.utils.SHA2Helper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.UUID;

@Controller
public class LoginController {

    public final UserService userService;

    public final UserTokenService userTokenService;

    public final AuthenticationService authService;

    @Autowired
    public LoginController(UserService userService, UserTokenService userTokenService, AuthenticationService authService) {
        this.userService = userService;
        this.userTokenService = userTokenService;
        this.authService = authService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/mobile-api/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse login(@RequestParam String eLogin,
                               @RequestParam String ePassword,
                               @RequestParam String base64PublicKey,
                               HttpServletRequest request) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        LoginResponse response = new LoginResponse();
        if (DESKey != null) {
            String dLogin = DESCryptoHelper.decrypt(DESKey, eLogin);
            String dPassword = DESCryptoHelper.decrypt(DESKey, ePassword);
            User user = userService.getByLogin(dLogin);
            if (user != null && new BCryptPasswordEncoder().matches(dPassword, user.getPassword())) {
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

                UserToken userToken = new UserToken(user,
                        userAgent.getBrowser().getName(),
                        userAgent.getOperatingSystem().getName(),
                        SHA2Helper.getSHA512String(UUID.randomUUID().toString(), "token"));

                userToken = userTokenService.createNewToken(userToken);

                response = new LoginResponse(userToken);
            } else {
                response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_PASSWORD.name());
            }
        } else {
            response = new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/checkToken", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse checkToken(@RequestParam String eToken,
                                    @RequestParam String base64PublicKey) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        LoginResponse response = new LoginResponse();
        if (DESKey != null) {
            String token = DESCryptoHelper.decrypt(DESKey, eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                if(token.equals(userToken.getToken())) {
                    response = new LoginResponse(userToken);
                } else {
                    response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_TOKEN.name());
                }
            } else {
                response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_TOKEN.name());
            }
        } else {
            response = new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
        return response;
    }

}

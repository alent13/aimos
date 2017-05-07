package com.applexis.aimos.controller;

import com.applexis.aimos.model.LoginResponse;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.entity.UserToken;
import com.applexis.aimos.model.service.AuthenticationService;
import com.applexis.aimos.model.service.UserService;
import com.applexis.aimos.model.service.UserTokenService;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.utils.crypto.AESCrypto;
import com.applexis.utils.crypto.HashHelper;
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
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        LoginResponse response;
        if (aes.getKey() != null) {
            String dLogin = aes.decrypt(eLogin);
            String dPassword = aes.decrypt(ePassword);
            User user = userService.getByLogin(dLogin);
            if (user != null && new BCryptPasswordEncoder().matches(dPassword, user.getPassword())) {
                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

                UserToken userToken = new UserToken(user,
                        userAgent.getBrowser().getName(),
                        userAgent.getOperatingSystem().getName(),
                        HashHelper.getSHA512String(UUID.randomUUID().toString(), "token"));

                userToken = userTokenService.createNewToken(userToken);

                response = new LoginResponse(userToken, aes);
            } else {
                response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_PASSWORD.name(), aes);
            }
        } else {
            response = new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

    @RequestMapping(value = "/mobile-api/checkToken", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse checkToken(@RequestParam String eToken,
                                    @RequestParam String base64PublicKey) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        LoginResponse response;
        if (aes.getKey() != null) {
            String token = aes.decrypt(eToken);
            UserToken userToken = userTokenService.getByToken(token);
            if (userToken != null) {
                if(token.equals(userToken.getToken())) {
                    response = new LoginResponse(userToken, aes);
                } else {
                    response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
                }
            } else {
                response = new LoginResponse(LoginResponse.ErrorType.INCORRECT_TOKEN.name(), aes);
            }
        } else {
            response = new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
        return response;
    }

}

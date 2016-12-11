package com.applexis.aimos.controller;

import com.applexis.aimos.model.LoginResponse;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.service.AuthenticationService;
import com.applexis.aimos.model.service.UserService;
import com.applexis.aimos.secureutils.DESCryptoHelper;
import com.applexis.aimos.secureutils.KeyExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Key;

@Controller
public class LoginController {

    @Autowired
    public UserService userService;

    @Autowired
    public AuthenticationService authService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/mobile-api/login", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse securedLogin(@RequestParam String login,
                                   @RequestParam String password,
                                   @RequestParam String base64PublicKey,
                                   HttpSession session) {
        Key DESKey = KeyExchange.getKey(base64PublicKey);
        LoginResponse response = new LoginResponse();
        if (DESKey != null) {
            String dLogin = DESCryptoHelper.decrypt(DESKey, login);
            String dPassword = DESCryptoHelper.decrypt(DESKey, password);
            UserDetails userDetails = authService.loadUserByUsername(dLogin);
            if(new BCryptPasswordEncoder().matches(dPassword, userDetails.getPassword())) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
                User user = userService.getByLogin(dLogin);
                response = new LoginResponse(user);
            }
        }
        response.setToken(session.getId());
        return response;
    }

}

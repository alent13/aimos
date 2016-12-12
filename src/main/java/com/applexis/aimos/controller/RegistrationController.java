package com.applexis.aimos.controller;

import com.applexis.aimos.model.LoginResponse;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
import com.applexis.aimos.utils.DESCryptoHelper;
import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.aimos.utils.SHAHashHelper;
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
import java.util.Objects;
import java.util.UUID;

@Controller
public class RegistrationController {

    private final UserService userService;

    private final UserExtraInfoService userExtraInfoService;

    private final UserSettingsService userSettingsService;

    private final UserTokenService userTokenService;

    private final ShowModeService showModeService;

    private final LanguageService languageService;

    @Autowired
    public RegistrationController(UserService userService,
                                  UserExtraInfoService userExtraInfoService,
                                  UserSettingsService userSettingsService,
                                  UserTokenService userTokenService,
                                  ShowModeService showModeService,
                                  LanguageService languageService) {
        this.userService = userService;
        this.userExtraInfoService = userExtraInfoService;
        this.userSettingsService = userSettingsService;
        this.userTokenService = userTokenService;
        this.showModeService = showModeService;
        this.languageService = languageService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/mobile-api/registration", method = RequestMethod.GET)
    @ResponseBody
    public LoginResponse registration(@RequestParam(value = "login") String eLogin,
                                         @RequestParam(value = "password") String ePassword,
                                         @RequestParam(value = "name") String eName,
                                         @RequestParam(value = "surname") String eSurname,
                                         @RequestParam(value = "email", required = false) String eEmail,
                                         @RequestParam(value = "phone", required = false) String ePhone,
                                         @RequestParam(value = "about_me", required = false) String eAbout,
                                         @RequestParam String base64PublicKey,
                                         HttpServletRequest request) {
        Key DESKey = KeyExchangeHelper.getKey(base64PublicKey);
        if (DESKey != null) {
            String login = DESCryptoHelper.decrypt(DESKey, eLogin);
            String password = DESCryptoHelper.decrypt(DESKey, ePassword);
            String name = DESCryptoHelper.decrypt(DESKey, eName);
            String surname = DESCryptoHelper.decrypt(DESKey, eSurname);
            String email = DESCryptoHelper.decrypt(DESKey, eEmail);
            String phone = DESCryptoHelper.decrypt(DESKey, ePhone);
            String about = DESCryptoHelper.decrypt(DESKey, eAbout);
            if (userService.getByLogin(login) == null) {
                User user = new User();
                user.setLogin(login);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                user.setName(name);
                user.setSurname(surname);

                ShowMode modeContactsOnly = showModeService.fingByMode(ShowMode.CONTACTSONLY);
                ShowMode modeEveryone = showModeService.fingByMode(ShowMode.EVERYONE);

                UserExtraInfo extraInfo = new UserExtraInfo();
                if (!Objects.equals(email, "") && email != null) {
                    extraInfo.setEmail(email);
                }
                if (!Objects.equals(phone, "") && phone != null) {
                    extraInfo.setPhone(phone);
                }
                if (!Objects.equals(about, "") && about != null) {
                    extraInfo.setAbout(about);
                }
                extraInfo = userExtraInfoService.create(extraInfo);
                user.setUserExtraInfo(extraInfo);

                Language lang = languageService.findByLang(Language.RU);

                UserSettings settings = new UserSettings();
                settings.setPageShow(modeEveryone);
                settings.setEmailShow(modeContactsOnly);
                settings.setPhoneShow(modeContactsOnly);
                settings.setAboutMeShow(modeContactsOnly);
                settings.setLang(lang);
                settings = userSettingsService.create(settings);
                user.setUserSettings(settings);

                user = userService.registrateNewUser(user);

                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

                UserToken userToken = new UserToken(user,
                        userAgent.getBrowser().getName(),
                        userAgent.getOperatingSystem().getName(),
                        SHAHashHelper.getSHA512String(UUID.randomUUID().toString(), "token"));

                userToken = userTokenService.createNewToken(userToken);

                return new LoginResponse(userToken);
            } else {
                return new LoginResponse(LoginResponse.ErrorType.USER_ALREADY_EXIST.name());
            }
        } else {
            return new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name());
        }
    }

}

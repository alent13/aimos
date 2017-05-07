package com.applexis.aimos.controller;

import com.applexis.aimos.model.LoginResponse;
import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
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
import java.util.Date;
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

    private final PlanService planService;

    @Autowired
    public RegistrationController(UserService userService,
                                  UserExtraInfoService userExtraInfoService,
                                  UserSettingsService userSettingsService,
                                  UserTokenService userTokenService,
                                  ShowModeService showModeService,
                                  LanguageService languageService,
                                  PlanService planService) {
        this.userService = userService;
        this.userExtraInfoService = userExtraInfoService;
        this.userSettingsService = userSettingsService;
        this.userTokenService = userTokenService;
        this.showModeService = showModeService;
        this.languageService = languageService;
        this.planService = planService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/mobile-api/registration", method = RequestMethod.POST)
    @ResponseBody
    public LoginResponse registration(@RequestParam(value = "eLogin") String eLogin,
                                         @RequestParam(value = "ePassword") String ePassword,
                                         @RequestParam(value = "eName") String eName,
                                         @RequestParam(value = "eSurname") String eSurname,
                                         @RequestParam(value = "eEmail", required = false) String eEmail,
                                         @RequestParam(value = "ePhone", required = false) String ePhone,
                                         @RequestParam(value = "eAbout", required = false) String eAbout,
                                         @RequestParam String base64PublicKey,
                                         HttpServletRequest request) {
        AESCrypto aes = new AESCrypto(KeyExchangeHelper.getInstance().getKey(base64PublicKey));
        if (aes.getKey() != null) {
            String login = aes.decrypt(eLogin);
            String password = aes.decrypt(ePassword);
            String name = aes.decrypt(eName);
            String surname = aes.decrypt(eSurname);
            String email = aes.decrypt(eEmail);
            String phone = aes.decrypt(ePhone);
            String about = aes.decrypt(eAbout);
            if (userService.getByLogin(login) == null) {
                User user = new User();
                user.setLogin(login);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                user.setName(name);
                user.setSurname(surname);
                user.setLastActionDatetime(new Date());
                user.setActive(true);

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

                user.setPlan(planService.getPlans().get(0));

                user = userService.registrateNewUser(user);

                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

                UserToken userToken = new UserToken(user,
                        userAgent.getBrowser().getName(),
                        userAgent.getOperatingSystem().getName(),
                        HashHelper.getSHA512String(UUID.randomUUID().toString(), "token"));

                userToken = userTokenService.createNewToken(userToken);

                return new LoginResponse(userToken, aes);
            } else {
                return new LoginResponse(LoginResponse.ErrorType.USER_ALREADY_EXIST.name(), aes);
            }
        } else {
            return new LoginResponse(LoginResponse.ErrorType.BAD_PUBLIC_KEY.name(), aes);
        }
    }

}

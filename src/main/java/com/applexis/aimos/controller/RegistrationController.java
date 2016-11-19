package com.applexis.aimos.controller;

import com.applexis.aimos.model.entity.*;
import com.applexis.aimos.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class RegistrationController {

    private final UserService userService;

    private final UserExtraInfoService userExtraInfoService;

    private final UserSettingsService userSettingsService;

    private final ShowModeService showModeService;

    private final LanguageService languageService;

    @Autowired
    public RegistrationController(UserService userService,
                                  UserExtraInfoService userExtraInfoService,
                                  UserSettingsService userSettingsService,
                                  ShowModeService showModeService,
                                  LanguageService languageService) {
        this.userService = userService;
        this.userExtraInfoService = userExtraInfoService;
        this.userSettingsService = userSettingsService;
        this.showModeService = showModeService;
        this.languageService = languageService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(@RequestParam(value = "login", required = true) String login,
                           @RequestParam(value = "password", required = true) String password,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "surname", required = false) String surname,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "phone", required = false) String phone,
                           @RequestParam(value = "about_me", required = false) String about_me,
                           Model model) {
        if(userService.getByLogin(login) == null){
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            if(!Objects.equals(name, "") && name != null) {
                user.setName(name);
            }
            if(!Objects.equals(surname, "") && surname != null) {
                user.setSurname(surname);
            }

            ShowMode modeContactsOnly = showModeService.fingByMode(ShowMode.CONTACTSONLY);
            ShowMode modeEveryone = showModeService.fingByMode(ShowMode.EVERYONE);

            UserExtraInfo extraInfo = new UserExtraInfo();
            if(!Objects.equals(email, "") && email != null) {
                extraInfo.setEmail(email);
            }
            if(!Objects.equals(phone, "") && phone != null) {
                extraInfo.setPhone(phone);
            }
            if(!Objects.equals(about_me, "") && about_me != null) {
                extraInfo.setAbout(about_me);
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

            userService.registrateNewUser(user);
            return "redirect:/login";
        }
        else {
            model.addAttribute("error", "Пользователь с таким логином уже зарегистрирован");
            return "registration";
        }
    }

}

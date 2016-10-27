package com.applexis.aimos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        Model model) {
        if(login == "applexis" && password == "asd") {
            return "messages";
        }
        else {
            model.addAttribute("authError", true);
            return "redirect:/";
        }
    }

}

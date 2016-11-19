package com.applexis.aimos.controller;

import com.applexis.aimos.model.service.ContactListService;
import com.applexis.aimos.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class MessagesController {

    @Autowired
    public UserService userService;

    @Autowired
    public ContactListService contactListService;

    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String allMessages(Model model, Principal principal) {
        model.addAttribute("login", principal.getName());
        Long userId = userService.getByLogin(principal.getName()).getId();
        model.addAttribute("contacts", contactListService.findFriends(userId));
        return "mindex";
    }

}

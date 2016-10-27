package com.applexis.aimos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessagesController {

    @RequestMapping(value = "/m", method = RequestMethod.GET)
    public String allMessages() {
        return "messages";
    }

}

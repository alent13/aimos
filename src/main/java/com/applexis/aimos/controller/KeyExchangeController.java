package com.applexis.aimos.controller;

import com.applexis.aimos.secureutils.KeyExchange;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KeyExchangeController {

    @RequestMapping(value = "/mobile-api/keyexchange", method = RequestMethod.GET)
    @ResponseBody
    public List<String> generateDESKey(@RequestParam String base64PublicKey) {
        List<String> key = new ArrayList<>();
        key.add(KeyExchange.generateDESKey(base64PublicKey));
        return key;
    }

}

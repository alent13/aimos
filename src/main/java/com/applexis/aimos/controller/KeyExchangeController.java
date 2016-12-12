package com.applexis.aimos.controller;

import com.applexis.aimos.utils.KeyExchangeHelper;
import com.applexis.aimos.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KeyExchangeController {

    @RequestMapping(value = "/mobile-api/keyExchange", method = RequestMethod.POST)
    @ResponseBody
    public String keyExchange(@RequestParam String base64PublicKey) {
        return StringUtils.surroundQuotes(KeyExchangeHelper.generateDESKey(base64PublicKey));
    }

}

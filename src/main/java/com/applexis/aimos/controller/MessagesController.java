package com.applexis.aimos.controller;

import com.applexis.aimos.model.MessageResponse;
import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.Message;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessagesController {

    @Autowired
    public UserService userService;

    @Autowired
    public DialogService dialogService;

    @Autowired
    public ContactListService contactListService;

    @Autowired
    public MessageService messageService;

    @Autowired
    public DialogUserService dialogUserService;

    @RequestMapping(value = "message", method = RequestMethod.GET)
    public String allMessages(Model model, Principal principal) {
        model.addAttribute("login", principal.getName());
        Long userId = userService.getByLogin(principal.getName()).getId();
        model.addAttribute("contacts", contactListService.findFriends(userId));
        return "mindex";
    }

    @RequestMapping(value = "/mobile-api/get_messages", method = RequestMethod.GET)
    @ResponseBody
    public List<Message> getMessages(@RequestParam(value = "dialog_id") Long id,
                                     @RequestParam(value = "token") String token,
                                     @RequestParam String base64PublicKey) {
        User user = userService.getByToken(token);
        Dialog dialog = dialogService.getDialog(id);
        if (user != null) {
            if (dialog != null && dialogUserService.getDialogByUser(user).contains(dialog)) {
                return messageService.getTop10(dialog);
            }
        }
        return new ArrayList<Message>();
    }

    @RequestMapping(value = "/mobile-api/send_message", method = RequestMethod.GET)
    @ResponseBody
    public MessageResponse sendMessages(@RequestParam(value = "message") String message,
                                        @RequestParam(value = "key") String key,
                                        @RequestParam(value = "dialog_id") Long id,
                                        @RequestParam(value = "token") String token,
                                        @RequestParam String base64PublicKey) {
        User user = userService.getByToken(token);
        Dialog dialog = dialogService.getDialog(id);
        if (user != null) {
            if (dialog != null && dialogUserService.getDialogByUser(user).contains(dialog)) {
                return new MessageResponse((messageService.sendMessage(new Message(user, dialog, key, message))));
            }
        }
        return new MessageResponse();
    }

}

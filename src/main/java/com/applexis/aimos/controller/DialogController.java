package com.applexis.aimos.controller;

import com.applexis.aimos.model.DialogResponse;
import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.DialogUser;
import com.applexis.aimos.model.entity.Status;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.StatusRepository;
import com.applexis.aimos.model.service.DialogService;
import com.applexis.aimos.model.service.DialogUserService;
import com.applexis.aimos.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DialogController {

    @Autowired
    public UserService userService;

    @Autowired
    public DialogService dialogService;

    @Autowired
    public DialogUserService dialogUserService;

    @Autowired
    public StatusRepository statusRepository;

    @RequestMapping(value = "/mobile-api/create_dialog", method = RequestMethod.GET)
    @ResponseBody
    public DialogResponse createDialog(@RequestParam(value = "dialog_name") String dialog_name,
                                       @RequestParam(value = "token") String token,
                                       @RequestParam String base64PublicKey) {
        User user = userService.getByToken(token);
        Dialog dialog = null;
        if (user != null) {
            dialog = new Dialog(dialog_name);
            dialog = dialogService.createDialog(dialog);
            DialogUser dialogUser = new DialogUser(dialog, user, statusRepository.findByStatus(Status.ADMIN));
            dialogUser = dialogUserService.create(dialogUser);
        }
        if(dialog != null) {
            return new DialogResponse(dialog, dialogUserService.getUserByDialog(dialog));
        } else {
            return new DialogResponse();
        }
    }

}

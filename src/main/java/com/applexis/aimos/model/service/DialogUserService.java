package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.DialogUser;
import com.applexis.aimos.model.entity.User;

import java.util.List;

public interface DialogUserService {

    DialogUser create(DialogUser dialogUser);

    List<Dialog> getDialogByUser(User user);

    List<User> getUserByDialog(Dialog dialog);

    DialogUser update(DialogUser dialogUser);

}

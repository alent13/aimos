package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.DialogUser;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.DialogUserRepository;
import com.applexis.aimos.model.service.DialogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DialogUserServiceImpl implements DialogUserService {

    @Autowired
    public DialogUserRepository repository;

    @Override
    public DialogUser create(DialogUser dialogUser) {
        return repository.save(dialogUser);
    }

    @Override
    public List<Dialog> getDialogByUser(User user) {
        List<DialogUser> dulist = repository.findDialogByUser(user);
        List<Dialog> dialogs = new ArrayList<>();
        for (DialogUser du : dulist) {
            dialogs.add(du.getDialog());
        }
        return dialogs;
    }

    @Override
    public List<User> getUserByDialog(Dialog dialog) {
        List<DialogUser> dulist = repository.findUserByDialog(dialog);
        List<User> users = new ArrayList<>();
        for (DialogUser du : dulist) {
            users.add(du.getUser());
        }
        return users;
    }

    @Override
    public DialogUser update(DialogUser dialogUser) {
        return repository.save(dialogUser);
    }
}

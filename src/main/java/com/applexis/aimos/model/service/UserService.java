package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.FSItem;
import com.applexis.aimos.model.entity.User;

import java.util.List;

public interface UserService {

    User registrateNewUser(User user);

    User getContact(Long id);
    User getContact(String login);

    List<User> getContacts(String loginStart);
    List<FSItem> getAllFiles(User user);

    void delete(User user);
    void delete(Long id);

    User update(User user);

    User getByLogin(String login);
    User getById(Long id);
}

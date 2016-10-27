package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.User;

public interface UserService {
    User registrateNewUser(User user);

    User getContacts(Long id);
    User getContacts(String login);

    void delete(User user);
    void delete(Long id);

    User update(User user);

    User getByLogin(String login);
}

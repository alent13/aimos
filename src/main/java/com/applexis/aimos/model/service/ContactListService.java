package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.ContactList;
import com.applexis.aimos.model.entity.User;

import java.util.List;

/**
 * @author applexis
 */
public interface ContactListService {

    public List<User> findFriends(Long id);

    public ContactList save(ContactList contactList);

}

package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.ContactList;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.ContactListRepository;
import com.applexis.aimos.model.service.ContactListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactListServiceImpl implements ContactListService {

    @Autowired
    public ContactListRepository repository;

    @Override
    public List<User> findFriends(Long id) {
        List<User> friends = new ArrayList<>();
        List<ContactList> contacts = repository.findFriendByUserId(id);
        for (ContactList contact : contacts) {
            friends.add(contact.getFriend());
        }
        return friends;
    }

    @Override
    public ContactList save(ContactList contactList) {
        return repository.save(contactList);
    }
}

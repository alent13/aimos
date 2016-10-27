package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.UserRepository;
import com.applexis.aimos.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public User registrateNewUser(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return repository.save(user);
    }

    @Override
    public User getContacts(Long id) {
        return repository.findOne(id);
    }

    @Override
    public User getContacts(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public User update(User user) {
        return repository.save(user);
    }

    @Override
    public User getByLogin(String login) {
        return repository.findByLogin(login);
    }
}

package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.entity.UserToken;
import com.applexis.aimos.model.repository.UserTokenRepository;
import com.applexis.aimos.model.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    public final UserTokenRepository repository;

    @Autowired
    public UserTokenServiceImpl(UserTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserToken createNewToken(UserToken userToken) {
        return repository.save(userToken);
    }

    @Override
    public void delete(UserToken userToken) {
        repository.delete(userToken);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public UserToken update(UserToken userToken) {
        return repository.save(userToken);
    }

    @Override
    public List<UserToken> getByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public UserToken getByToken(String token) {
        return repository.findByToken(token);
    }
}

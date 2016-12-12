package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.entity.UserToken;

import java.util.List;

/**
 * @author applexis
 */
public interface UserTokenService {

    UserToken createNewToken(UserToken userToken);

    void delete(UserToken userToken);
    void delete(Long id);

    UserToken update(UserToken userToken);

    List<UserToken> getByUser(User user);
    UserToken getByToken(String token);

}

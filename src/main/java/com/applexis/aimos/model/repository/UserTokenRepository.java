package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findByToken(String token);

    List<UserToken> findByUser(User user);

}

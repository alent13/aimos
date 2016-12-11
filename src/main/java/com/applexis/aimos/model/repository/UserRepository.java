package com.applexis.aimos.model.repository;


import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByToken(String token);

    List<User> findByLoginContaining(String login);

}

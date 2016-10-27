package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactListRepository extends JpaRepository<User, Long> {
}

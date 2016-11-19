package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.ContactList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactListRepository extends JpaRepository<ContactList, Long> {

    List<ContactList> findFriendByUserId(Long userId);

}

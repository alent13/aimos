package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.DialogUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author applexis
 */
public interface DialogUserRepository extends JpaRepository<DialogUser, Long> {

    //List<DialogUser> findByIdDialog(Long id);

}

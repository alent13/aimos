package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.DialogUser;
import com.applexis.aimos.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author applexis
 */
public interface DialogUserRepository extends JpaRepository<DialogUser, Long> {

    List<DialogUser> findDialogByUser(User user);

    List<DialogUser> findUserByDialog(Dialog dialog);

}

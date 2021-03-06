package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findFirst10ByDialogOrderByIdDesc(Dialog dialog);

}

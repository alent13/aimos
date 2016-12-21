package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogRepository extends JpaRepository<Dialog, Long> {

    Dialog findByName(String name);

}

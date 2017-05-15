package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.AccessMode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessModeRepository extends JpaRepository<AccessMode, Long> {

    AccessMode findByAccessMode(String accessMode);

}

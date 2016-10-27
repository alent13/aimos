package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.ShowMode;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author applexis
 */
public interface ShowModeRepository extends JpaRepository<ShowMode, Long> {

    ShowMode findByMode(String mode);

}

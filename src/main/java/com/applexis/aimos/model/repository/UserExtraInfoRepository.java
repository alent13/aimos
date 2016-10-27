package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.UserExtraInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author applexis
 */
public interface UserExtraInfoRepository extends JpaRepository<UserExtraInfo, Long> {
}

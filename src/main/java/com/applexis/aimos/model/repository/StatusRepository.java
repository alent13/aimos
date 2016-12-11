package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Status;
import org.springframework.data.repository.CrudRepository;

/**
 * @author applexis
 */
public interface StatusRepository extends CrudRepository<Status, Long> {

    Status findByStatus(String status);

}

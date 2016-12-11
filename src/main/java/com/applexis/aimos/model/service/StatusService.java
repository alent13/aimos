package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Status;

/**
 * @author applexis
 */
public interface StatusService {

    Status getByStatus(String status);

    Status getById(Long id);

}

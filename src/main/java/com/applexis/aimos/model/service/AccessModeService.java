package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.AccessMode;

/**
 * @author applexis
 */
public interface AccessModeService {

    AccessMode create(AccessMode accessMode);

    void delete(AccessMode accessMode);
    void delete(Long id);

    AccessMode update(AccessMode accessMode);

    AccessMode findById(Long id);
    AccessMode findByAccessMode(String accessMode);

}

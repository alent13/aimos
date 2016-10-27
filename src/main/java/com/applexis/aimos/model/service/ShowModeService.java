package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.ShowMode;

/**
 * @author applexis
 */
public interface ShowModeService {

    ShowMode create(ShowMode mode);

    void delete(ShowMode mode);
    void delete(Long id);

    ShowMode update(ShowMode mode);

    ShowMode findById(Long id);
    ShowMode fingByMode(String mode);

}

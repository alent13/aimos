package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Language;

/**
 * @author applexis
 */
public interface LanguageService {

    Language create(Language lang);

    void delete(Language lang);
    void delete(Long id);

    Language update(Language lang);

    Language findById(Long id);
    Language findByLang(String lang);

}

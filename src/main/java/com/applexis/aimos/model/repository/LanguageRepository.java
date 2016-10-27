package com.applexis.aimos.model.repository;

import com.applexis.aimos.model.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Language findByLang(String lang);

}

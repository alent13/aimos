package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Language;
import com.applexis.aimos.model.repository.LanguageRepository;
import com.applexis.aimos.model.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository repository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Language create(Language lang) {
        return repository.save(lang);
    }

    @Override
    public void delete(Language lang) {
        repository.delete(lang);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Language update(Language lang) {
        return repository.save(lang);
    }

    @Override
    public Language findById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Language findByLang(String lang) {
        return repository.findByLang(lang);
    }
}

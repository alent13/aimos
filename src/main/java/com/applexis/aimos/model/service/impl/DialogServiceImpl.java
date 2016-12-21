package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.repository.DialogRepository;
import com.applexis.aimos.model.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DialogServiceImpl implements DialogService {

    @Autowired
    public DialogRepository repository;

    @Override
    public Dialog createDialog(Dialog dialog) {
        return repository.save(dialog);
    }

    @Override
    public Dialog getDialog(Long id) {
        return repository.findOne(id);
    }

    @Override
    public void delete(Dialog user) {
        repository.delete(user);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Dialog update(Dialog dialog) {
        return repository.save(dialog);
    }

    @Override
    public Dialog findByName(String name) {
        return repository.findByName(name);
    }
}

package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.FSItem;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.DirectoryRepository;
import com.applexis.aimos.model.service.DialogService;
import com.applexis.aimos.model.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    @Autowired
    DirectoryRepository repository;

    @Override
    public Directory getById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public List<Directory> getChildDirectories(Long id, User user) {
        return repository.findByParentIdAndUser(id, user);
    }

    @Override
    public List<Directory> getUserRootDirectories(User user) {
        return repository.findByParentIdAndUser(0L, user);
    }

    @Override
    public void delete(Directory directory) {
        repository.delete(directory);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public Directory create(Directory directory) {
        return repository.save(directory);
    }

    @Override
    public Directory update(Directory directory) {
        return repository.save(directory);
    }
}

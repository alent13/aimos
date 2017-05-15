package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.File;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.FileRepository;
import com.applexis.aimos.model.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by applexis on 08.04.2017.
 */

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository repository;

    @Override
    public File getById(Long id) {
        return repository.findOne(id);
    }

    @Override
    public File getByPath(String path, String name, User user) {
        return repository.findFirstByPathAndNameAndUser(path, name, user);
    }

    @Override
    public List<File> getUserRootFiles(User user) {
        return repository.findByParentDirectoryAndUser(0L, user);
    }

    @Override
    public void delete(File file) {
        repository.delete(file);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public File create(File file) {
        return repository.save(file);
    }

    @Override
    public File update(File file) {
        return repository.save(file);
    }
}

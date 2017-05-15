package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.File;
import com.applexis.aimos.model.entity.User;

import java.util.List;

public interface FileService {

    File getById(Long id);
    File getByPath(String path, String name, User user);

    List<File> getUserRootFiles(User user);

    void delete(File file);
    void delete(Long id);

    File create(File file);
    File update(File file);

}

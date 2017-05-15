package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.FSItem;
import com.applexis.aimos.model.entity.User;

import java.util.List;

public interface DirectoryService {

    Directory getById(Long id);

    List<Directory> getChildDirectories(Long id, User user);
    List<Directory> getUserRootDirectories(User user);

    void delete(Directory directory);
    void delete(Long id);

    Directory create(Directory directory);
    Directory update(Directory directory);

}

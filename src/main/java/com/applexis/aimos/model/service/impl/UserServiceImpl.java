package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Directory;
import com.applexis.aimos.model.entity.FSItem;
import com.applexis.aimos.model.entity.File;
import com.applexis.aimos.model.entity.User;
import com.applexis.aimos.model.repository.DirectoryRepository;
import com.applexis.aimos.model.repository.FileRepository;
import com.applexis.aimos.model.repository.UserRepository;
import com.applexis.aimos.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final FileRepository fileRepository;
    private final DirectoryRepository directoryRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           FileRepository fileRepository,
                           DirectoryRepository directoryRepository){
        this.repository = repository;
        this.fileRepository = fileRepository;
        this.directoryRepository = directoryRepository;
    }

    @Override
    public User registrateNewUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getContact(Long id) {
        return repository.findOne(id);
    }

    @Override
    public User getContact(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public List<User> getContacts(String loginStart) {
        return repository.findByLoginContaining(loginStart);
    }

    @Override
    public List<FSItem> getAllFiles(User user) {
        List<FSItem> fsList = new ArrayList<>();
        List<Directory> rootDirs = directoryRepository.findByParentIdAndUser(0L, user);
        List<File> rootFiles = fileRepository.findByParentDirectoryAndUser(0L, user);
        fsList.addAll(rootDirs);
        fsList.addAll(rootFiles);
        Stack<Directory> dirIdStack = new Stack<>();
        dirIdStack.addAll(rootDirs);
        while (dirIdStack.size() > 0) {
            Directory cDir = dirIdStack.pop();
            List<Directory> tmpDirs = directoryRepository.findByParentIdAndUser(cDir.getId(), user);
            List<File> tmpFiles = fileRepository.findByParentDirectoryAndUser(cDir.getId(), user);
            fsList.addAll(tmpDirs);
            fsList.addAll(tmpFiles);
            dirIdStack.addAll(tmpDirs);
        }
        return fsList;
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public User update(User user) {
        return repository.save(user);
    }

    @Override
    public User getByLogin(String login) {
        return repository.findByLogin(login);
    }

    @Override
    public User getById(Long id) {
        return repository.findOne(id);
    }
}

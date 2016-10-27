package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.UserExtraInfo;
import com.applexis.aimos.model.repository.UserExtraInfoRepository;
import com.applexis.aimos.model.service.UserExtraInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserExtraInfoServiceImpl implements UserExtraInfoService {

    private final UserExtraInfoRepository repository;

    @Autowired
    public UserExtraInfoServiceImpl(UserExtraInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserExtraInfo create(UserExtraInfo extraInfo) {
        return repository.save(extraInfo);
    }

    @Override
    public void delete(UserExtraInfo extraInfo) {
        repository.delete(extraInfo);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public UserExtraInfo update(UserExtraInfo extraInfo) {
        return repository.save(extraInfo);
    }

    @Override
    public UserExtraInfo findById(Long id) {
        return repository.findOne(id);
    }
}

package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.UserExtraInfo;

public interface UserExtraInfoService {

    UserExtraInfo create(UserExtraInfo extraInfo);

    void delete(UserExtraInfo extraInfo);
    void delete(Long id);

    UserExtraInfo update(UserExtraInfo extraInfo);

    UserExtraInfo findById(Long id);

}

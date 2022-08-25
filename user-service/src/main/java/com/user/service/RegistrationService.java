package com.user.service;


import com.user.entity.UserEntity;

import java.util.Map;

public interface RegistrationService {
    Map<String,Object> save(UserEntity userEntity);

    Map<String, Object> verifyAccount(String email, String password);

    public Map<String, Object> forgetPassword(String email);

    public Map<String, Object> resetPassword(String email, String password, String oldpassword);
}

package com.user.service;

import com.user.entity.UserEntity;

import java.util.Map;

public interface UserService {

    Map<String, Object> getById(Long id);

    Map<String, Object> update(UserEntity userEntity);

    Map<String, Object> delete(Long id);

    Map<String, Object> updateEmail(Long id,String oldEmail, String newEmail);
}

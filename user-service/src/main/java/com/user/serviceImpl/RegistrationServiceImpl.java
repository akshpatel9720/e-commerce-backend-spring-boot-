package com.user.serviceImpl;


import com.user.entity.UserEntity;
import com.user.repository.UserRepository;
import com.user.service.EmailService;
import com.user.service.RegistrationService;
import com.user.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    public static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Override
    public Map<String, Object> save(UserEntity userEntity) {
        Map<String, Object> map = new HashMap<>();
        UserEntity savedUser = new UserEntity();
        if (userEntity != null) {
            Optional<UserEntity> user = userRepository.findOneByEmailIgnoreCase(userEntity.getEmail());
            if (user.isPresent()) {
                map.put("status", Boolean.FALSE);
                map.put("message", "email already exist");
                map.put("data", new ArrayList<>());
            } else {
                savedUser.setActive(Boolean.FALSE);
                savedUser.setCreatedDate(LocalDateTime.now());
                savedUser.setEmail(userEntity.getEmail());
                savedUser.setFirstName(userEntity.getFirstName());
                savedUser.setLastName(userEntity.getLastName());
                savedUser.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                savedUser.setPhoneNumber(userEntity.getPhoneNumber());
                savedUser.setUpdatedDate(null);
                userRepository.save(savedUser);
                emailService.sendWelcomeMailToUser(userEntity);
                map.put("status", Boolean.TRUE);
                map.put("message", "data saved successfully");
                map.put("data", savedUser);

            }
        } else {
            map.put("status", Boolean.FALSE);
            map.put("message", "user entity is null");
            map.put("data", new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> verifyAccount(String email, String password) {
        Map<String, Object> map = new HashMap<>();

        Optional<UserEntity> userEntity = userRepository.findOneByEmailIgnoreCase(email);
        if (userEntity.isPresent()) {
            UserEntity userEntity1 = userEntity.get();
            if (userEntity1.getPassword().equalsIgnoreCase(password)) {
                userEntity1.setActive(true);
                userRepository.save(userEntity1);
                map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
                map.put(ResponseMessage.MESSAGE, ResponseMessage.USER_VERIFICATION_SUCCESS);
                map.put(ResponseMessage.DATA, new ArrayList<>());
            }
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.USER_VERIFICATION_FAILURE);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> forgetPassword(String email) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> userEntity = userRepository.findOneByEmailIgnoreCase(email);
        if (userEntity.isPresent()) {
            UserEntity userEntity1 = userEntity.get();
            if (userEntity1.getActive().equals(Boolean.TRUE)) {
                emailService.sendPasswordResetMailToUser(userEntity1);
                map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
                map.put(ResponseMessage.MESSAGE, ResponseMessage.FORGOT_PASSWORD_SUCCESS);
                map.put(ResponseMessage.DATA, new ArrayList<>());
            } else {
                map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
                map.put(ResponseMessage.MESSAGE, ResponseMessage.LOGIN_NOT_VERIFIED);
                map.put(ResponseMessage.DATA, new ArrayList<>());
            }
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FORGOT_PASSWORD_EMAIL_NOT_EXISTS);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> resetPassword(String email, String password, String oldpassword) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> userEntity = userRepository.findOneByEmailIgnoreCase(email);
        if (userEntity.isPresent()) {
            UserEntity userEntity1 = userEntity.get();
            if (userEntity1.getPassword().equalsIgnoreCase(oldpassword)) {
                userEntity1.setPassword(passwordEncoder.encode(password));
                userRepository.save(userEntity1);
                map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
                map.put(ResponseMessage.MESSAGE, ResponseMessage.RESET_PASSWORD_SUCCESS);
                map.put(ResponseMessage.DATA, new ArrayList<>());
            } else {
                map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
                map.put(ResponseMessage.MESSAGE, ResponseMessage.OLD_PASSWORD_NOT_MATCHED);
                map.put(ResponseMessage.DATA, new ArrayList<>());
            }
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.USER_NOT_FOUND);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }
}

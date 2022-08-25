package com.user.serviceImpl;

import com.user.entity.UserEntity;
import com.user.repository.UserRepository;
import com.user.service.EmailService;
import com.user.service.UserService;
import com.user.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public Map<String, Object> getById(Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user1 = userRepository.findById(id);
        if (user1.isPresent()) {

            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_GET);
            map.put(ResponseMessage.DATA, user1);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_GET);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> update(UserEntity userEntity) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> existingUserEntity = userRepository.findById(userEntity.getId());
        if (existingUserEntity.isPresent()) {
            existingUserEntity.get().setUpdatedDate(LocalDateTime.now());
            existingUserEntity.get().setPhoneNumber(userEntity.getPhoneNumber());
            existingUserEntity.get().setFirstName(userEntity.getFirstName());
            existingUserEntity.get().setLastName(userEntity.getLastName());
            UserEntity updateUser = userRepository.save(existingUserEntity.get());
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_UPDATE);
            map.put(ResponseMessage.DATA, updateUser);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSSAGE_UPDATE);
        }
        return map;
    }

    @Override
    public Map<String, Object> delete(Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_DELETE);
            userRepository.deleteById(id);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_DELETE);
        }
        return map;
    }

    @Override
    public Map<String, Object> updateEmail(Long id, String oldEmail, String newEmail) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setEmail(newEmail);
            userEntity.setUpdatedDate(LocalDateTime.now());
            userEntity.setActive(Boolean.TRUE);
            userRepository.save(userEntity);
            emailService.sendMailToUpdateEmail(userEntity);
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_UPDATE_EMAIL);
            map.put(ResponseMessage.DATA, userEntity);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_DELETE_EMAIL);
            map.put(ResponseMessage.DATA,new ArrayList<>());
        }
        return map;
    }
}

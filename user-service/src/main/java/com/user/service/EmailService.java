package com.user.service;

import com.user.entity.UserEntity;

public interface EmailService {
    public void sendWelcomeMailToUser(UserEntity userEntity);

    public void sendPasswordResetMailToUser(UserEntity userEntity);

    public void sendMailToUpdateEmail(UserEntity userEntity);

}

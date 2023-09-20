package com.becoder.service;

import com.becoder.model.AuthenticationProvider;
import com.becoder.model.UserDtls;


public interface UserService {
    public UserDtls createUser(UserDtls user,String url);
    public UserDtls getUserByEmail(String email);

    public boolean checkEmail(String email);

    public void removeSessionMessage();
    public void sendEmail(UserDtls user, String path);
    public boolean verifyAccount(String verificationCode);
    public void updateResetPasswordToken(String token, String email) throws UserDtlsNotFoundException;
    public UserDtls getByResetPasswordToken(String token);
    public void updatePassword(UserDtls user, String newPassword);
    public UserDtls createUserAfterOAuthLoginSuccess(String email,String name, AuthenticationProvider provider);
    public UserDtls updateUserAfterOAuthLoginSuccess(UserDtls user ,String name);
}

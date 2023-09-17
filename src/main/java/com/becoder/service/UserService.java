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
    public void createUserAfterOAuthLoginSuccess(String email,String name, AuthenticationProvider provider);
    public void updateUserAfterOAuthLoginSuccess(UserDtls user ,String name);
}

package com.becoder.service;

import com.becoder.model.AuthenticationProvider;
import com.becoder.model.UserDtls;
import com.becoder.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserDtls createUser(UserDtls user,String url) {
       String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole("ROLE_USER");
        user.setEnable(false);
        user.setVerificationCode(UUID.randomUUID().toString());
        UserDtls newuser = userRepo.save(user);

        if (newuser != null) {
            sendEmail(newuser, url);
        }
        return newuser;
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public boolean checkEmail(String email) {

        return userRepo.existsByEmail(email);
    }

    @Override
    public void removeSessionMessage() {

        HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
                .getSession();

        session.removeAttribute("msg");
    }

    @Override
    public void sendEmail(UserDtls user, String url) {
        String from = "phucvinh710@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verfication";
        String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "Vinh";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(from, "Vinhhuynh");
            helper.setTo(to);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFullName());
            String siteUrl = url + "/verify?code=" + user.getVerificationCode();

            System.out.println(siteUrl);

            content = content.replace("[[URL]]", siteUrl);

            helper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyAccount(String verificationCode) {

        UserDtls user = userRepo.findByVerificationCode(verificationCode);

        if (user == null) {
            return false;
        } else {

            user.setEnable(true);
            user.setVerificationCode(null);

            userRepo.save(user);

            return true;
        }

    }

    public void updateResetPasswordToken(String token, String email) throws UserDtlsNotFoundException {
        UserDtls customer = userRepo.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            userRepo.save(customer);
        } else {
            throw new UserDtlsNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public UserDtls getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(UserDtls user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

    public UserDtls createUserAfterOAuthLoginSuccess(String email,String name, AuthenticationProvider provider) {
        UserDtls user = new UserDtls();
        user.setEmail(email);
        user.setFullName(name);
        user.setAuthProvider(provider);
        user.setEnable(true);
        user.setRole("ROLE_USER");
        return userRepo.save(user);
    }
    public UserDtls updateUserAfterOAuthLoginSuccess(UserDtls user ,String name) {
        user.setFullName(name);
        user.setAuthProvider(AuthenticationProvider.GOOGLE);

      return userRepo.save(user);
    }

}

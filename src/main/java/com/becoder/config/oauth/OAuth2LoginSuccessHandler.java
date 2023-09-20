package com.becoder.config.oauth;

import com.becoder.model.AuthenticationProvider;
import com.becoder.model.UserDtls;
import com.becoder.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    private UserDetailsService userDetailsService;
    @Autowired
    HttpSession session;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User =(CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();
        System.out.println("email: " + email);
        System.out.println("name: " + name);
        UserDtls user = userService.getUserByEmail(email);
        if(user == null){
            UserDtls user1 = userService.createUserAfterOAuthLoginSuccess(email,name,AuthenticationProvider.GOOGLE);
            if(user1.getRole().contains("ROLE_USER")) {
                response.sendRedirect("/user/profile");
            }else {
                response.sendRedirect("/admin/profile");
            }
        }else {
            UserDtls user1 = userService.updateUserAfterOAuthLoginSuccess(user,name);

            if(user1.getRole().contains("ROLE_USER")) {
                response.sendRedirect("/user/profile");
            }else {
                response.sendRedirect("/admin/profile");
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

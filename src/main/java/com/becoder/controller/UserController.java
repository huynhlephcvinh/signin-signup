package com.becoder.controller;

import com.becoder.model.UserDtls;
import com.becoder.repository.UserRepository;
import com.becoder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    HttpSession session;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void commonUser(Principal p, Model m,@AuthenticationPrincipal OAuth2User usero2) {
        if (p != null) {
            String email = p.getName();
            UserDtls user = userRepo.findByEmail(email);
            m.addAttribute("user", user);
        }
        if(usero2 != null) {
            String email = usero2.getAttribute("email");
            UserDtls user = userRepo.findByEmail(email);
            m.addAttribute("user", user);
        }



    }

    @GetMapping("/profile")
    public String profile(Model model) {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
//            DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
//            model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
//        }else {
//            UserDtls user = (UserDtls) securityContext.getAuthentication().getPrincipal();
//            UserDtls users = userService.getUserByEmail(user.getEmail());
//            model.addAttribute("userDetails", users.getFullName());
//        }

        return "profile";
    }
}

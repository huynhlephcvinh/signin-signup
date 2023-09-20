package com.becoder.controller;

import com.becoder.model.UserDtls;
import com.becoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private UserRepository userRepo;

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
    public String profile() {
        return "staff";
    }
}
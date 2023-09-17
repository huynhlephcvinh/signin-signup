package com.becoder.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserDtls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;

    private String email;

    private String address;

    private String qualification;

    private String password;

    private String role;

    private boolean enable;

    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;
}

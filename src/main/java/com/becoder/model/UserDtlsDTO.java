package com.becoder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtlsDTO {
    private Integer id;
    private String address;
    private String email;
    private String fullName;
    private String password;
    private String qualification;
    private String role;



}

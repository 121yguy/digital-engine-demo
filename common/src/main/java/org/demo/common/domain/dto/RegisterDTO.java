package org.demo.common.domain.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String phone;
}

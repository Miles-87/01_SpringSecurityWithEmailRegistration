package com.app.dto.security;

import com.app.model.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String passwordConfirmation;
    private Boolean enabled;
    private Role role;
}

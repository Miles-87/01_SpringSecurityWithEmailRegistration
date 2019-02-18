package com.app.validators;

import com.app.dto.security.UserDto;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidator {

    private UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ValidationErrors validate(UserDto userDto) {

        ValidationErrors errors = new ValidationErrors();

        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            errors.addError("username", "USER WITH GIVEN USERNAME ALREADY EXISTS");
        }

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            errors.addError("email", "USER WITH GIVEN EMAIL ALREADY EXISTS");
        }

        if (!Objects.equals(userDto.getPassword(), userDto.getPasswordConfirmation())) {
            errors.addError("password", "PASSWORD AND CONFIRMED PASSWORD MUST BE THE SAME");
        }

        return errors;
    }
}

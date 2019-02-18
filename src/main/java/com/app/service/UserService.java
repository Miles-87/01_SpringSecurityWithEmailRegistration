package com.app.service;

import com.app.dto.MyModelMapper;
import com.app.dto.security.UserDto;
import com.app.exception.MyException;
import com.app.model.security.Role;
import com.app.model.security.User;
import com.app.model.security.VerificationToken;
import com.app.repository.UserRepository;
import com.app.repository.VerificationTokenRepository;
import com.app.service.listener.OnRegistrartionCompleteEvent;
import com.app.validators.UserValidator;
import com.app.validators.ValidationErrors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private MyModelMapper modelMapper;
    private UserValidator userValidator;
    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher applicationEventPublisher;

    public UserService(
            UserRepository userRepository,
            VerificationTokenRepository verificationTokenRepository, MyModelMapper modelMapper,
            UserValidator userValidator,
            PasswordEncoder passwordEncoder, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public UserDto registerNewUser(UserDto userDto, HttpServletRequest request) {
        try {
            // WALIDACJA
            ValidationErrors validationErrors = userValidator.validate(userDto);
            if (validationErrors.hasErrors()) {
                throw new IllegalArgumentException("VALIDATION ERRORS: " + validationErrors.errors());
            }
            User user = modelMapper.fromUserDtoToUser(userDto);
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.ADMIN);
            User userFromDb = userRepository.save(user);

            String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
            applicationEventPublisher.publishEvent(new OnRegistrartionCompleteEvent(userFromDb, url));

            return modelMapper.fromUserToUserDto(userFromDb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, REGISTER USER EXCEPTION", LocalDateTime.now());
        }
    }

    // metoda generuje wpis do db ktory przekazanemu jako argument userowi przyporzadkowuje
    // wygenerowany token
    public VerificationToken verificationToken(User user) {
        try {
            return verificationTokenRepository.save(VerificationToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .expirationDateTime(LocalDateTime.now().plusMinutes(5))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, CREATE VERIFICATION TOKEN EXCEPTION", LocalDateTime.now());
        }
    }

    // na podstawie otrzymanego jako argument tokena sprawdzamy czy istnieje user
    // przyporzadkowany takiemu tokenowi i jezeli tak to zostanie on aktywowany
    public void activateUser(String token) {
        try {
            VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

            if (verificationToken == null) {
                throw new NullPointerException("TOKEN IS NOT CORRECT");
            }

            if (verificationToken.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("TOKEN HAS BEEN EXPIRED");
            }

            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException("SERVICE, CREATE VERIFICATION TOKEN EXCEPTION", LocalDateTime.now());
        }
    }

}

package com.app.security;

import com.app.exception.MyException;
import com.app.model.security.Role;
import com.app.model.security.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

// zadaniem tego beana bedzie przerabianie uzytkownikow z db na
// obiektu specjalnego typu ktorymi zarzadzal bedzie mechanizm security

@Service
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("USER WITH GIVEN USERNAME DOESN'T EXIST");
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    getRole(user.getRole())
            );

        } catch (Exception e) {
            throw new MyException("LOAD USER BY USERNAME: " + e.getMessage(), LocalDateTime.now());
        }
    }

    private Collection<GrantedAuthority> getRole(Role role) {
        return Arrays.asList(new SimpleGrantedAuthority(role.getName()));
    }
}

package com.app.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class MyWebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    public MyWebSecurity(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // 1234
        /*
        auth
                .inMemoryAuthentication()
                .withUser("u").password("$2a$10$0oPF/AoryFlMOaeXZZMKmeXoz0Yih.8658byEQl4lzI0xJb50vEBO").roles("USER")
                .and()
                .withUser("a").password("$2a$10$0oPF/AoryFlMOaeXZZMKmeXoz0Yih.8658byEQl4lzI0xJb50vEBO").roles("USER", "ADMIN");
        */

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/producers/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/products/**").hasAnyRole("ADMIN")
                .antMatchers("/", "/users/new", "/webjars/**", "/users/registrationConfirm").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login").permitAll() // to zadanie bedzie wykonywane kiedy nastapi koniecznosc logowania
                .loginProcessingUrl("/app-login") // w formularzu logowania bedziesz za pomoca metody post musial wykonac zadanie /app-login
                .usernameParameter("username") // pod takim parametrem ma zostac przekazana nazwa uzytkownika z formularza logowania
                .passwordParameter("password") // pod takim parametrem ma zostac przekazane haslo uztkownika z formularza logowania
                .defaultSuccessUrl("/", true) // takie zadanie ma zostac wykonane kiedy sie juz zalogujesz
                .failureUrl("/login/error") // takie zadanie ma zostac wykonane kiedy logowanie nie powiedzie sie

                .and()
                .logout()                       // rozpoczynamy konfiguracje wylogowania
                .logoutUrl("/app-logout")       // takie zadanie ma wyslac formularz logowania
                .clearAuthentication(true)      // czyscimy wszelkie dane autentykacyjne
                .logoutSuccessUrl("/login")    // po wylogowaniu mamy wykonac zadanie /login

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(
                    HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse,
                    AccessDeniedException e) throws IOException, ServletException {
                      httpServletResponse.sendRedirect("/accessDenied");
            }
        };
    }
}

package com.learning.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.learning.scm.services.implementations.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    // user create and login using in memory service

    // @Bean
    // public UserDetailsService userDetailsService() {

    // // this user is generated and stored in memory, not in database
    // UserDetails user1 =
    // User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN",
    // "USER").build();
    // UserDetails user2 =
    // User.withDefaultPasswordEncoder().username("admin2").password("admin2").build();

    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1,user2);
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    // configuration for authentication provider for spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail service ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        // password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register", "/services",
            // "/about").permitAll();

            // user se start hone wale sabhi URL protected hai, baaki sab permit all
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // using form default login page
        // httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login"); // humara custom login page aajayega
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            /*
             * formLogin.successHandler(new AuthenticationSuccessHandler() {
             * 
             * @Override
             * public void onAuthenticationSuccess(HttpServletRequest request,
             * HttpServletResponse response,
             * Authentication authentication) throws IOException, ServletException {
             * // TODO Auto-generated method stub
             * throw new
             * UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'"
             * );
             * }
             * 
             * });
             * 
             * formLogin.failureHandler(new AuthenticationFailureHandler() {
             * 
             * @Override
             * public void onAuthenticationFailure(HttpServletRequest request,
             * HttpServletResponse response,
             * AuthenticationException exception) throws IOException, ServletException {
             * // TODO Auto-generated method stub
             * throw new
             * UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'"
             * );
             * }
             * 
             * });
             */
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

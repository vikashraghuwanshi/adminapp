package com.shopme.adminapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig{

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/users/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/brands/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin",
                                "Salesperson","Editor", "Shipper")
                        .requestMatchers("/questions/**").hasAnyAuthority("Admin", "Assistant")
                        .requestMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")
                        .requestMatchers("/customers/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/shipping/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/orders/**").hasAnyAuthority("Admin",
                                "Salesperson", "Shipper")
                        .requestMatchers("/reports/**").hasAnyAuthority("Admin", "Salesperson")
                        .requestMatchers("/articles/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/menus/**").hasAnyAuthority("Admin", "Editor")
                        .requestMatchers("/settings/**").hasAuthority("Admin")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                )
                        .logout().permitAll()
                .and()
                .rememberMe().userDetailsService(this.userDetailsService());

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}

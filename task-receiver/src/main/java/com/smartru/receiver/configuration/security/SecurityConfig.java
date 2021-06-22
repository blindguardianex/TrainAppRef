package com.smartru.receiver.configuration.security;

import com.smartru.receiver.configuration.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PERMIT_ALL_ENDPOINTS = new String[]{"/api/sign",
                                                                    "/api/refresh_token"};
    private static final String[] AUTHENTICATED_ENDPOINTS = new String[]{"/api/task/add",
                                                                    "/api/task/all",
                                                                    "/api/task/get",
                                                                    "/api/test/auth"};
    private static final String[] ADMIN_ENDPOINTS = new String[]{"/api/for_admin"};

    @Autowired
    private UserDetailsService detailsService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers(PERMIT_ALL_ENDPOINTS)
                        .permitAll()
                    .antMatchers(AUTHENTICATED_ENDPOINTS)
                        .authenticated()
                    .antMatchers(ADMIN_ENDPOINTS)
                        .hasAuthority("ADMIN")
                    .anyRequest()
                        .denyAll();
    }
}

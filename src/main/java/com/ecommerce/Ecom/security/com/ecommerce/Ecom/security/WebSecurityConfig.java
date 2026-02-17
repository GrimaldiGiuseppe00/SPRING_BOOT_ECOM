package com.ecommerce.Ecom.security.com.ecommerce.Ecom.security;

import com.ecommerce.Ecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.Ecom.security.jwt.AuthTokenFilter;
import com.ecommerce.Ecom.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
//@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilterBean() {
        return new AuthTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProviderBean() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    public AuthenticationManager authenticationManager(AuthenticationConfiguration  authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
    }
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.requestMatchers("/h2-console/**").
                permitAll().
                requestMatchers("/api/signin").permitAll().
                        requestMatchers("/api/auth/**").permitAll().
                        requestMatchers("/v3/api-docs/**").permitAll().
                        requestMatchers("/swagger-ui/**").permitAll().
                        requestMatchers("/api/public/**").permitAll().
                        requestMatchers("/api/admin/**").permitAll().
                        requestMatchers("/api/test/**").permitAll().
                        requestMatchers("/iamges/**").permitAll().
                anyRequest().
                authenticated());
        http.authenticationProvider(authenticationProviderBean());
        http.addFilterBefore(authenticationJwtTokenFilterBean(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web ->  web.ignoring().requestMatchers(
                "v2/api-docs",
                            "/configuration/ui",
                            "/swagger-ui.html",
                            "/swagger-ui-html",
                            "/webjars/**"

        ));
    }

}

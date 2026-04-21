package com.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {

	  @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	  
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	        http
	            .csrf(csrf -> csrf.disable())

	            .sessionManagement(session ->
	                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            )

	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers(
	                    "/auth/register",
	                    "/auth/login",
	                    "/error",
	                    "/auth/verify",
	                    "/swagger-ui/**",
	                    "/api-docs/**"
	                ).permitAll()

	                .anyRequest().authenticated()
	            )

	            .formLogin(form -> form.disable())
	            .httpBasic(basic -> basic.disable());

	        return http.build();
	    }
}

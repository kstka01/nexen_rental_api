package com.nexentire.rental.util.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http.cors()
		.and()
		.csrf()
		.disable()
		.httpBasic()
		.disable()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/resources/*", "/js/*", "/css/*", "/", "/export/*", "/api/authenticate.do").permitAll()
		.anyRequest()
		.authenticated();
		
		http.headers().frameOptions().disable();
		
		http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}

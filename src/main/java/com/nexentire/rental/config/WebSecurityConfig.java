package com.nexentire.rental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.nexentire.rental.filter.HMacCheckFilter;
import com.nexentire.rental.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	HMacCheckFilter hmacCheckFilter;

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
		.antMatchers("/resources/*", "/js/*", "/css/*", "/", "/export/*", "/v1/apis/api/admin/authenticate.do", "/v1/apis/api/vendor/*").permitAll()
		.anyRequest()
		.authenticated();
		
		http.headers().frameOptions().disable();
		
		http.addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.addFilterAfter(hmacCheckFilter, UsernamePasswordAuthenticationFilter.class);
	}

}

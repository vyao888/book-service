package com.canoo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.AuthenticationEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private AuthenticationEntryPoint authEntryPoint;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserDetails user = User.withUsername("user").password(encoder.encode("user")).roles("USER").build();
		UserDetails admin = User.withUsername("admin").password(encoder.encode("admin")).roles("USER", "ADMIN").build();
		auth.inMemoryAuthentication().withUser(user).withUser(admin).passwordEncoder(encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/book/delete/**").hasRole("ADMIN")
				 .antMatchers("/book/**")
				 .permitAll();
				//.and().httpBasic().authenticationEntryPoint(authEntryPoint);
	}

}

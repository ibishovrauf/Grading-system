package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.example.demo.Models.Authority;
import com.example.demo.Models.User;
import com.example.demo.Services.AuthorityService;
import com.example.demo.Services.UserService;

@Configuration
public class ProjectConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Autowired
	AuthorityService authorityService;

	@Override 
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/home", "/js/**", "/css/**", "/activate-account", "/group-subjects", "/api/example").permitAll()
			.antMatchers("/admin/create-group").hasAnyAuthority("groupAdmin")
			.antMatchers("/admin/My-group", "/admin/student-arrangement", "/admin/group-subjects", "/admin/add-subjects", "/admin/create-subject", "/admin/create-exams", "/admin/add-grades").hasAnyAuthority("groupAdmin", "activeGroupAdmin")
			.antMatchers("/admin/**").hasAnyAuthority("Admin")
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll()
			.and()
			.logout().permitAll();
	}

	@Override 
    protected void configure(AuthenticationManagerBuilder auth) throws	Exception { 
		// User user = new User();
		// user.setEnable(true);
		// user.setUsername("admin");
		// user.setPassword("12345678");

		// Authority authority = new Authority();
		// authority.setRoleCode("Admin");
		// authority.setRoleDescription("Admin can do anything");
		// user.setAuthorities(List.of(authority));
		// userService.register(user);
		
		// Authority authority1 = new Authority();
		// authority1.setRoleCode("groupAdmin");
		// authority1.setRoleDescription("groupAdmin can create group");
		// authorityService.createAuthority(authority1);

		// Authority authority2 = new Authority();
		// authority2.setRoleCode("activeGroupAdmin");
		// authority2.setRoleDescription("activeGroupAdmin can do anything");
		// authorityService.createAuthority(authority2);

		// Authority authority3 = new Authority();
		// authority3.setRoleCode("User");
		// authority3.setRoleDescription("User");
		// authorityService.createAuthority(authority3);
		
		auth.userDetailsService(userService); 
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder(); 
	}
}

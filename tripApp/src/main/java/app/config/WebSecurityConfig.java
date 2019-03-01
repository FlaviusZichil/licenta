package app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// Setting Service to find User in the database.
		// And Setting PassswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/register", "/login").permitAll();
//		http.authorizeRequests().antMatchers("/userHome").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/userHome").access("hasRole('ROLE_USER')").anyRequest().authenticated();
		http.authorizeRequests().antMatchers("/adminHome").access("hasRole('ROLE_ADMIN')");

		// Config for Login Form
		http.authorizeRequests().and().formLogin()
				.loginProcessingUrl("/j_spring_security_check")
				.loginPage("/login")//
				.defaultSuccessUrl("/all")
				.failureUrl("/login")
				.usernameParameter("email")
				.passwordParameter("password");
	}
}


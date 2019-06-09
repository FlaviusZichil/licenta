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
		
		http.authorizeRequests().antMatchers("/tombola-results").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/users-reports").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/user-details").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/statistics").access("hasRole('ROLE_ADMIN')");
			
		http.authorizeRequests().antMatchers("/my-articles").access("hasRole('ROLE_STAFF')");	
		
		http.authorizeRequests().antMatchers("/add-article").access("hasRole('ROLE_STAFF') or hasRole('ROLE_GUIDE')");
		http.authorizeRequests().antMatchers("/achievements").access("hasRole('ROLE_STAFF') or hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/add-trip").access("hasRole('ROLE_USER') or hasRole('ROLE_GUIDE') or hasRole('ROLE_STAFF')");
		http.authorizeRequests().antMatchers("/personal-data").access("hasRole('ROLE_USER') or hasRole('ROLE_GUIDE') or hasRole('ROLE_STAFF')");	
	
		http.authorizeRequests().antMatchers("/trip-details").authenticated();
		http.authorizeRequests().antMatchers("/all-trips").authenticated();
		http.authorizeRequests().antMatchers("/my-trips").authenticated();
		http.authorizeRequests().antMatchers("/closest-trips").authenticated();
		http.authorizeRequests().antMatchers("/tombola").authenticated();
		http.authorizeRequests().antMatchers("/my-points").authenticated();
		http.authorizeRequests().antMatchers("/all-articles").authenticated();
		http.authorizeRequests().antMatchers("/article").authenticated();
		http.authorizeRequests().antMatchers("/guides").authenticated();

		// Config for Login Form
		http.authorizeRequests().and().formLogin()
				.loginProcessingUrl("/j_spring_security_check")
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.failureUrl("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
	}
}


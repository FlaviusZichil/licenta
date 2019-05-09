package app.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.config.EncrytedPasswordUtils;
import app.entities.Role;
import app.entities.UserEntity;
import app.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = getUserByName(username);

		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}

		Role role = this.getUserRole(user);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		
		if (role != null) {
			GrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
			grantList.add(authority);	
		}
		
		UserDetails userDetails = (UserDetails) new User(user.getEmail(),  EncrytedPasswordUtils.encrytePassword(user.getPassword()), grantList);
		return userDetails;
	}
	
	private UserEntity getUserByName(String userName) {
		Iterable<UserEntity> allUsers = userRepository.findAll();

		for (UserEntity user : allUsers) {
			if (user.getEmail().equals(userName)) {
				return user;
			}
		}
		return null;
	}

	private Role getUserRole(UserEntity user) {
		if (user != null) {
			return user.getRole();
		}
		return null;
	}
}

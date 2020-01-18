package com.calm.pdd.core.services.user;

import com.calm.pdd.core.exceptions.EmailExistsException;
import com.calm.pdd.core.exceptions.UserExistsException;
import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.enums.Role;
import com.calm.pdd.core.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserRegistrationService, UserDetailsService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public User registerNewUser(final UserDto userDto) throws UserExistsException, EmailExistsException {
		checkForUsernameAndEmailExisting(userDto);
		
		User user = new User()
				.setUsername(userDto.getUsername())
				.setEmail(userDto.getEmail())
				.setPassword(passwordEncoder.encode(userDto.getPassword()))
				.setActive(true)
				.addRole(Role.USER);
		
		return userRepository.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User with login [%s] not found", username)));
	}
	
	private void checkForUsernameAndEmailExisting(final UserDto userDto) throws UserExistsException, EmailExistsException {
		if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new UserExistsException(String.format("Login %s already taken", userDto.getUsername()));
		}
		
		if(userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			throw new EmailExistsException(String.format("Email %s already taken", userDto.getEmail()));
		}
	}
}

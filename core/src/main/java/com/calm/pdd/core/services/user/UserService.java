package com.calm.pdd.core.services.user;

import com.calm.pdd.core.exceptions.EmailExistsException;
import com.calm.pdd.core.exceptions.UserExistsException;
import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService, UserDetailsService {
	
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public User registerNewUser(final UserDto userDto) throws UserExistsException, EmailExistsException {
		checkForUsernameAndEmailExisting(userDto);
		
		return userRepository.save(new User(
				userDto.getUsername(),
				userDto.getEmail(),
				userDto.getPassword()
				//passwordEncoder.encode(userDto.getPassword()) //изза того что в коре захуячил секьюрити зависимость, все по пизде и оно тут автоконфигурится второй раз
				//upd: убрал нахер отсюда, оставит только в вебе, но всеравно просит логин... штош надо учить секьюрити щас
		));
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

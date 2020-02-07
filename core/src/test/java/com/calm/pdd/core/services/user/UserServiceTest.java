package com.calm.pdd.core.services.user;

import com.calm.pdd.core.exceptions.EmailExistsException;
import com.calm.pdd.core.exceptions.UserExistsException;
import com.calm.pdd.core.model.dto.UserDto;
import com.calm.pdd.core.model.entity.User;
import com.calm.pdd.core.model.entity.UserStatistic;
import com.calm.pdd.core.model.enums.Role;
import com.calm.pdd.core.model.repository.UserRepository;
import com.calm.pdd.core.model.repository.UserStatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	private UserStatisticRepository userStatisticRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private User userMock;
	
	private UserDto userDto;
	
	private UserService userService;
	
	@Captor
	private ArgumentCaptor<User> userCaptor;
	
	@BeforeEach
	public void setUp() {
		userDto = new UserDto();
		userDto.setUsername("john");
		userDto.setEmail("john@gmail.com");
		userDto.setPassword("123456");
		
		userService = new UserService(userRepository, passwordEncoder, userStatisticRepository);
	}
	
	@Test
	void successRegisterNewUser() throws UserExistsException, EmailExistsException {
		when(passwordEncoder.encode("123456")).thenReturn("123456xxx");
		
		userService.registerNewUser(userDto);
		
		verify(userRepository).save(userCaptor.capture());
		User user = userCaptor.getValue();
		
		assertThat(user.getUsername()).isEqualTo("john");
		assertThat(user.getEmail()).isEqualTo("john@gmail.com");
		assertThat(user.getPassword()).isEqualTo("123456xxx");
		assertThat(user.isActive()).isTrue();
		assertThat(user.getRoles()).contains(Role.USER);
		
		verify(userStatisticRepository).save(isA(UserStatistic.class));
	}
	
	@Test
	void checkUsernameExists(){
		userDto.setUsername("carl");
		when(userRepository.findByUsername("carl")).thenReturn(Optional.of(userMock));
		assertThrows(UserExistsException.class, () -> userService.registerNewUser(userDto));
	}
	
	@Test
	void checkEmailExists() {
		userDto.setEmail("carl@gmail.com");
		when(userRepository.findByEmail("carl@gmail.com")).thenReturn(Optional.of(userMock));
		assertThrows(EmailExistsException.class, () -> userService.registerNewUser(userDto));
	}
	
	@Test
	void successLoadUser() {
		when(userRepository.findByUsername("john")).thenReturn(Optional.of(userMock));
		UserDetails userDetails = userService.loadUserByUsername("john");
		assertThat(userDetails).isEqualTo(userMock);
	}
	
	@Test
	void loadUserButNotFound() {
		when(userRepository.findByUsername("carl")).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("carl"));
	}
}

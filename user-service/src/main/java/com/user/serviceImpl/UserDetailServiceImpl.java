package com.user.serviceImpl;



import com.user.entity.UserEntity;
import com.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("customUserService")
public class UserDetailServiceImpl implements UserDetailsService {

	public static final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("inside loadUserByUsername method");
		Optional<UserEntity> entity = userRepository.findOneByEmailIgnoreCase(username);
		UserEntity userEntity = null;
		if(entity.isPresent()) {
			userEntity = entity.get();
		}
		if (userEntity == null) {
			throw new UsernameNotFoundException("", new Throwable("Invalid Creds"));
		}
		UserDetails user = User.withUsername(userEntity.getEmail()).password(userEntity.getPassword())
				.authorities("USER").build();
		return user;
	}
}
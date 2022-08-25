package com.user.controller;

import com.user.DTO.RequestDTO;
import com.user.DTO.ResponseDTO;
import com.user.config.JwtTokenUtil;
import com.user.entity.UserEntity;
import com.user.repository.UserRepository;
import com.user.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class SingInController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    public SingInController(UserDetailsService jwtInMemoryUserDetailsService) {
        this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseDTO createAuthenticationToken(@RequestBody RequestDTO authenticationRequest)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> mapResponse = new HashMap<String,Object>();
        Map<String, Object> mapResponseUserData = new HashMap<String, Object>();
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            mapResponse.put("token", token);

            Optional<UserEntity> userEntity = userRepository.findOneByEmailIgnoreCase(userDetails.getUsername());

            if (userEntity.isPresent()) {
                UserEntity userData = userEntity.get();

                //RefreshToken refreshToken = refreshTokenService.createRefreshToken(userData.getId());
                mapResponseUserData.put("id", userData.getId().toString());
                mapResponseUserData.put("email", userData.getEmail());
                mapResponseUserData.put("token", token);
                map.put("RESPONSE_DATA", mapResponseUserData);

            }
            return new ResponseDTO(ResponseMessage.SUCCESS_API_CODE,Boolean.TRUE,map);
        } catch (Exception e) {
            if (e.getMessage().equalsIgnoreCase("INVALID_CREDENTIALS")) {
                map.put("RESPONSE_STATUS", "400");
                map.put("RESPONSE_MESSAGE", "LOGIN_INVALID_CREDENTIALS");
                map.put("RESPONSE_DATA", new ArrayList<>());
            }
        }

        return new ResponseDTO(ResponseMessage.FAIl_SIGNIN_MESSAGE,Boolean.FALSE,map);
    }


    private void authenticate(String name, String password) throws Exception {
//        Objects.requireNonNull(name);
//        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception(ResponseMessage.LOGIN_INVALID_CREDENTIALS, e);
        }
    }

}

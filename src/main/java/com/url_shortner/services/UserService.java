package com.url_shortner.services;

import com.url_shortner.dtos.LoginRequest;
import com.url_shortner.models.User;
import com.url_shortner.repo.UserRepository;
import com.url_shortner.security.jwt.JwtAuthenticationResponse;
import com.url_shortner.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
        private PasswordEncoder passwordEncoder;
        private UserRepository userRepository;
        private AuthenticationManager authenticationManager;
        private JwtUtils jwtUtils;

        //signup
    public User registerUser(User user) {
user.setPassword(passwordEncoder.encode(user.getPassword()));
return userRepository.save(user);
    }

//login
    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

//retrieve user info based on the  unique username saved in the database
    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("Username not found" + name));
    }
//logout the user
    public void logoutUser(){
        SecurityContextHolder.clearContext();
    }
}

package com.url_shortner.controllers;
import com.url_shortner.dtos.LoginRequest;
import com.url_shortner.dtos.RegisterRequest;
import com.url_shortner.models.User;
import com.url_shortner.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
private UserService userService;


@PostMapping("/public/login")
public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
return ResponseEntity.ok(userService.authenticateUser(loginRequest));
}
@PostMapping("/public/logout")
public ResponseEntity<?> logoutUser(){
    userService.logoutUser();
    return ResponseEntity.ok("Logout successful");
}

@PostMapping("/public/register")
    public ResponseEntity<?> auth (@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER");
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
}

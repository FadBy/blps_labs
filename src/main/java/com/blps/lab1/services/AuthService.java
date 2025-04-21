package com.blps.lab1.services;

import com.blps.lab1.dto.LoginRequest;
import com.blps.lab1.dto.RegisterRequest;
import com.blps.lab1.dto.AuthResponse;
import com.blps.lab1.entities.User;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.repositories.RoleRepository;
import com.blps.lab1.repositories.UserRepository;
import com.blps.lab1.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationService applicationService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RequestInvalidException("Username is already in use");
        }

        var userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RequestInvalidException("Role USER not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);

        String desired = request.getRole().getDescription();
        if ("ROLE_ADMIN".equalsIgnoreCase(desired) || "ROLE_SUPER_ADMIN".equalsIgnoreCase(desired)) {
            sendApplication(user.getUsername(), desired);
        }

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        if (!authentication.isAuthenticated()) {
            throw new RequestInvalidException("Invalid username or password");
        }

        String jwtToken = jwtService.generateToken((User)authentication.getPrincipal());
        return new AuthResponse(jwtToken);
    }

    public void sendApplication(String username, String roleName) {
        applicationService.apply(username, roleName);
    }
}

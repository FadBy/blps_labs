package com.blps.lab1.security;

import com.blps.lab1.entities.User;
import com.blps.lab1.repositories.RoleRepository;
import com.blps.lab1.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        var userOpt = userRepository.findByUsername(username);
        return userOpt.orElseGet(() -> User.builder()
                .username("")
                .password("")
                .roles(List.of(roleRepository.findByName("USER").get()))
                .build());

    }
}

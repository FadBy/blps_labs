package com.blps.lab1.configs;

import com.blps.lab1.entities.Privilege;
import com.blps.lab1.entities.Role;
import com.blps.lab1.entities.User;
import com.blps.lab1.repositories.PrivilegeRepository;
import com.blps.lab1.repositories.RoleRepository;
import com.blps.lab1.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Privilege writeVacancy       = createPrivilegeIfNotFound("WRITE_VACANCY");
        Privilege writeEmployee      = createPrivilegeIfNotFound("WRITE_EMPLOYEE");
        Privilege receiveReport      = createPrivilegeIfNotFound("RECEIVE_REPORT");
        Privilege readReviews        = createPrivilegeIfNotFound("READ_REVIEWS");
        Privilege writeReviews       = createPrivilegeIfNotFound("WRITE_REVIEWS");
        Privilege answerReviews      = createPrivilegeIfNotFound("ANSWER_REVIEWS");
        Privilege approveApplication = createPrivilegeIfNotFound("APPROVE_APPLICATION");
        Privilege readApplication    = createPrivilegeIfNotFound("READ_APPLICATION");

        List<Privilege> userPrivileges       = List.of(readReviews, writeReviews);
        List<Privilege> adminPrivileges      = List.of(receiveReport, answerReviews);
        List<Privilege> superAdminPrivileges = List.of(writeEmployee, writeVacancy, approveApplication, readApplication);

        List<Privilege> adminAndUser = Stream.concat(
                adminPrivileges.stream(), userPrivileges.stream()
        ).collect(Collectors.toList());

        List<Privilege> allPrivileges = Stream.concat(
                superAdminPrivileges.stream(), adminAndUser.stream()
        ).collect(Collectors.toList());

        // Создаем роли, если их нет
        createRoleIfNotFound("ROLE_SUPER_ADMIN", allPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminAndUser);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        // Создаем дефолтного супер-админа
        Role superAdminRole = roleRepository.findByName("ROLE_SUPER_ADMIN")
                .orElseThrow(() -> new IllegalStateException("ROLE_SUPER_ADMIN not found after creation"));

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("pass123"))
                    .roles(List.of(superAdminRole))
                    .build();
            userRepository.save(admin);
        }

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(String name) {
        return privilegeRepository.findByName(name)
                .orElseGet(() -> privilegeRepository.save(
                        Privilege.builder().name(name).build()));
    }

    @Transactional
    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(name).privileges(privileges).build()));
    }
}

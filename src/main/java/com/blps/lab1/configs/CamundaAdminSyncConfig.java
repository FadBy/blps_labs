package com.blps.lab1.configs;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CamundaAdminSyncConfig {

    @Bean
    public CommandLineRunner createCamundaAdminUser(IdentityService identityService) {
        return args -> {
            String adminId = "admin";

            if (identityService.createUserQuery().userId(adminId).singleResult() == null) {
                org.camunda.bpm.engine.identity.User user = identityService.newUser(adminId);
                user.setPassword("admin");
                identityService.saveUser(user);
            }

            if (identityService.createGroupQuery().groupId("camunda-admin").singleResult() == null) {
                Group group = identityService.newGroup("camunda-admin");
                group.setName("Administrators");
                group.setType("SYSTEM");
                identityService.saveGroup(group);
            }

            List<org.camunda.bpm.engine.identity.User> admins = identityService.createUserQuery().memberOfGroup("camunda-admin").list();
            boolean alreadyInGroup = admins.stream().anyMatch(u -> u.getId().equals(adminId));
            if (!alreadyInGroup) {
                identityService.createMembership(adminId, "camunda-admin");
            }
        };
    }
}


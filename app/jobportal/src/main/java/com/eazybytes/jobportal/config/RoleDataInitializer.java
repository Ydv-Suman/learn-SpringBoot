package com.eazybytes.jobportal.config;

import com.eazybytes.jobportal.constants.ApplicationConstants;
import com.eazybytes.jobportal.entity.Role;
import com.eazybytes.jobportal.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private static final String SYSTEM_USER = "SYSTEM";

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRole(ApplicationConstants.ROLE_JOB_SEEKER);
        seedRole("ROLE_EMPLOYER");
        seedRole("ROLE_ADMIN");
    }

    private void seedRole(String roleName) {
        roleRepository.findRoleByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setCreatedBy(SYSTEM_USER);
            return roleRepository.save(role);
        });
    }
}

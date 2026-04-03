package com.finance.dashboard;

import com.finance.dashboard.model.Role;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.RoleRepository;
import com.finance.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) {
        // check if admin exists, if not create one
        if(userRepository.findByUsername("admin").isEmpty()) {

            Role adminRole = roleRepository.findById(1L)  // create ADMIN role if not exists
                    .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
            // create default admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("12345"));   // encode password
            admin.setActive(true);
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            System.out.println("ADMIN created");
        }
    }
}
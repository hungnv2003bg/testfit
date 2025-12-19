package com.foxconn.sopchecklist.service;

import com.foxconn.sopchecklist.entity.Role;
import com.foxconn.sopchecklist.entity.Users;
import com.foxconn.sopchecklist.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {

        if (!roleService.existsByName("ADMIN")) {
            Role adminRole = new Role("ADMIN", "Administrator role with full access");
            roleService.save(adminRole);
        }


        if (!roleService.existsByName("MANAGER")) {
            Role managerRole = new Role("MANAGER", "Manager role with management access");
            roleService.save(managerRole);
        }


        if (!roleService.existsByName("USER")) {
            Role userRole = new Role("USER", "User role with basic access");
            roleService.save(userRole);
        }
    }

    private void initializeAdminUser() {
        String adminManv = "admin";
        if (!usersService.existsByManv(adminManv)) {
            Users adminUser = new Users();
            adminUser.setManv(adminManv);
            adminUser.setFullName("Administrator");
            adminUser.setEmail("admin@foxconn.com"); // Dummy email
            adminUser.setPasswordHash(passwordEncoder.encode("123456"));
            adminUser.setStatus(UserStatus.ACTIVE);
            
            Set<Role> roles = new HashSet<>();
            Role adminRole = roleService.findByName("ADMIN");
            if (adminRole != null) {
                roles.add(adminRole);
            }
            adminUser.setRoles(roles);
            
            usersService.save(adminUser);
            System.out.println("Admin user created successfully: admin / 123456");
        }
    }
}


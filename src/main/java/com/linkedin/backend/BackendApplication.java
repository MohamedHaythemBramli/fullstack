package com.linkedin.backend;

import com.linkedin.backend.entities.Role;
import com.linkedin.backend.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableCaching
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            insertRole(roleRepository, 1, "Admin");
            insertRole(roleRepository, 2, "USER");
            insertRole(roleRepository, 3, "Manager");
        };
    }

    @Transactional
    public void insertRole(RoleRepository roleRepository, Integer id, String name) {
        if (!roleRepository.existsById(id)) {
            Role role = new Role();
            role.setId(id);
            role.setCreatedDate(LocalDateTime.now());
            role.setLastModifiedDate(LocalDateTime.now());
            role.setName(name);
            roleRepository.save(role);
        }
    }*/

}

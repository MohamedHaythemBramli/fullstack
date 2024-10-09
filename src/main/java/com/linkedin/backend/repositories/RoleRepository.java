package com.linkedin.backend.repositories;

import com.linkedin.backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findRoleByName(String name);
}

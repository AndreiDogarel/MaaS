package com.example.maas.repository;

import com.example.maas.entities.Role;
import com.example.maas.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles getByName(Role roleName);
}

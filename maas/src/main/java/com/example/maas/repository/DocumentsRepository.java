package com.example.maas.repository;


import com.example.maas.entities.Documents;
import com.example.maas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    boolean existsByParent_IdAndCdIsNotNull(Long parentId);
    boolean existsByParent_IdAndPcIsNotNull(Long parentId);
    Optional<Documents> findByParent(User parent);
}

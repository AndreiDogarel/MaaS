package com.example.maas.repository;


import com.example.maas.entities.Documents;
import com.example.maas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DocumentsRepository extends JpaRepository<Documents, Long> {

    @Query("SELECT COUNT(*) FROM Documents d where d.parent.id = :parentId and d.pcName is not null and d.cdName is not null")
    int hasValidDocuments(@Param("parentId") Long parentId);
//    boolean existsByParent_IdAndPcIsNotNull(Long parentId);
    Optional<Documents> findByParent(User parent);
}

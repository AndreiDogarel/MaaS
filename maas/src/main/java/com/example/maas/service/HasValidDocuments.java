package com.example.maas.service;

import com.example.maas.entities.User;
import com.example.maas.repository.DocumentsRepository;
import com.example.maas.repository.UserRepository;
import org.springframework.stereotype.Component;


@Component
public class HasValidDocuments {
    // For now, it checks if user has Id and Driving License uploaded

    private final DocumentsRepository documentsRepository;
    private final UserRepository userRepository;

    public HasValidDocuments(DocumentsRepository documentsRepository, UserRepository userRepository) {
        this.documentsRepository = documentsRepository;
        this.userRepository = userRepository;
    }

    public boolean check(Long userId) {
        if(userId == null) {
            return false;
        }

        try {
            User user = userRepository.findById(userId).orElse(null);
            if(user == null) {
                return false;
            }
            return documentsRepository.existsByParent_IdAndCdIsNotNull(userId) &&
                    documentsRepository.existsByParent_IdAndPcIsNotNull(userId);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkByUsername(String username) {
        if(username == null || username.isBlank()) {
            return false;
        }

        return userRepository.findByUsername(username)
                .map(User::getId)
                .map(this::check)
                .orElse(false);
    }


}



package com.example.maas.service;

import com.example.maas.entities.Documents;
import com.example.maas.entities.User;
import com.example.maas.repository.DocumentsRepository;
import com.example.maas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DocumentsService {
    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private UserRepository userRepository;
    
    public void uploadFile(MultipartFile file, String documentType) throws IOException {
        byte[] bytes = file.getBytes();
//            aici am incercat sa folosesc google vision, dar am ajuns in punctul in care posibil sa trebuiasca sa
//            platim putin ca sa folosim chestia asta asa ca o pun pe hold momentan
//            System.out.println(ocrService.extractTextFromBytes(bytes));
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(file.getInputStream(), uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);
        System.out.println(documentType);
        if(documentType.equals("IDENTITY_CARD")) {
            System.out.println(fileName);
            uploadIC(file, userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        } else if (documentType.equals("DRIVING_LICENCE")) {
            System.out.println(fileName);
            uploadDL(file, userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        }

    }

    public void uploadIC(MultipartFile file, User user) throws IOException {
        User parent = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Documents doc = (Documents) documentsRepository.findByParent(user)
                .orElse(null);

        if (doc == null) {
            doc = new Documents();
            doc.setParent(parent);
        }

        doc.setCdName(file.getOriginalFilename());
        doc.setCd(file.getBytes());
        doc.setUploadDateCd(LocalDateTime.now());

        documentsRepository.save(doc);
    }

    public void uploadDL(MultipartFile file, User user) throws IOException {
        User parent = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Documents doc = (Documents) documentsRepository.findByParent(user)
                .orElse(null);

        if (doc == null) {
            doc = new Documents();
            doc.setParent(parent);
        }

        doc.setPcName(file.getOriginalFilename());
        doc.setPc(file.getBytes());
        doc.setUploadDatePc(LocalDateTime.now());

        documentsRepository.save(doc);
    }
}

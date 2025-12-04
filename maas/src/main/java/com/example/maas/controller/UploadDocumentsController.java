package com.example.maas.controller;

import com.example.maas.repository.UserRepository;
import com.example.maas.service.DocumentsService;
import com.example.maas.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class UploadDocumentsController {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        try {
            documentsService.uploadFile(file, documentType);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}

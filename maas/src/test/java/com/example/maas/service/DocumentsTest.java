package com.example.maas.service;

import com.example.maas.entities.Documents;
import com.example.maas.entities.User;
import com.example.maas.repository.DocumentsRepository;
import com.example.maas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentsTest {

    @Mock
    private DocumentsRepository documentsRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DocumentsService documentsService;

    @Test
    void uploadIC_savesCdNameAndBytesAndTimestamp() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "id-card.png", "image/png", "fake-image-bytes".getBytes());
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(documentsRepository.findByParent(user)).thenReturn(Optional.empty());

        ArgumentCaptor<Documents> captor = ArgumentCaptor.forClass(Documents.class);
        when(documentsRepository.save(any(Documents.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        documentsService.uploadIC(file, user);

        // Assert
        verify(documentsRepository).save(captor.capture());
        Documents saved = captor.getValue();
        assertEquals("id-card.png", saved.getCdName());
        assertArrayEquals(file.getBytes(), saved.getCd());
        assertNotNull(saved.getUploadDateCd(), "uploadDateCd should be set");
        // Parent should be set to the found user (parent.id == user.id)
        assertNotNull(saved.getParent());
        assertEquals(user.getId(), saved.getParent().getId());
    }

    @Test
    void uploadDL_updatesExistingDocumentsAndSavesPcFields() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file", "driving-license.pdf", "application/pdf", "pdf-bytes".getBytes());
        User user = new User();
        user.setId(2L);

        Documents existing = new Documents();
        existing.setParent(user); // simulate found entry

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(documentsRepository.findByParent(user)).thenReturn(Optional.of(existing));
        when(documentsRepository.save(any(Documents.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Documents> captor = ArgumentCaptor.forClass(Documents.class);

        // Act
        documentsService.uploadDL(file, user);

        // Assert
        verify(documentsRepository).save(captor.capture());
        Documents saved = captor.getValue();
        assertEquals("driving-license.pdf", saved.getPcName());
        assertArrayEquals(file.getBytes(), saved.getPc());
        assertNotNull(saved.getUploadDatePc(), "uploadDatePc should be set");
        assertEquals(user.getId(), saved.getParent().getId());
    }

    @Test
    void uploadIC_throwsWhenUserNotFound() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "x.png", "image/png", "x".getBytes());
        User user = new User();
        user.setId(999L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> documentsService.uploadIC(file, user));
    }
}

package com.example.maas.service;

import com.example.maas.controller.AdminController;
import com.example.maas.entities.Role;
import com.example.maas.entities.Roles;
import com.example.maas.entities.User;
import com.example.maas.repository.RoleRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private AdminController adminController;

    private Class<?> updateUserRequestClass;
    private Class<?> userDetailsDtoClass;

    @BeforeEach
    void setUp() {
        updateUserRequestClass = findInnerClass("UpdateUserRequest");
        userDetailsDtoClass = findInnerClass("UserDetailsDto");
    }

    @Test
    void deleteUser_deletesAndReturnsMessage() {
        System.out.println("Running deleteUser_deletesAndReturnsMessage");
        Map<String, String> result = adminController.deleteUser(42L);

        assertEquals("User deleted successfully", result.get("message"));
        verify(userRepository).deleteById(42L);
    }

    @Test
    void deleteDecommissionedVehicles_callsRepository() {
        System.out.println("Running deleteDecommissionedVehicles_callsRepository");
        String result = adminController.deleteDecommissionedVehicles();

        assertEquals("Decommissioned vehicles deleted successfully", result);
        verify(vehicleRepository).deleteDecommissionedVehicles();
    }

    @Test
    void getAllUsers_mapsEntitiesToDtos() {
        System.out.println("Running getAllUsers_mapsEntitiesToDtos");
        Roles adminRole = new Roles(1L, Role.ADMIN);
        Roles customerRole = new Roles(2L, Role.CUSTOMER);

        User admin = User.builder().id(1L).username("admin").role(adminRole).build();
        User customer = User.builder().id(2L).username("cust").role(customerRole).build();
        when(userRepository.findAll()).thenReturn(List.of(admin, customer));

        List<?> results = adminController.getAllUsers();

        assertEquals(2, results.size());
        assertEquals(1L, extractId(results.get(0)));
        assertEquals("admin", extractUsername(results.get(0)));
        assertEquals("ADMIN", extractRole(results.get(0)));
        assertEquals(2L, extractId(results.get(1)));
        assertEquals("cust", extractUsername(results.get(1)));
        assertEquals("CUSTOMER", extractRole(results.get(1)));
    }

    @Test
    void updateUser_updatesUsernameAndRoleAndSaves() {
        System.out.println("Running updateUser_updatesUsernameAndRoleAndSaves");
        Roles currentRole = new Roles(2L, Role.CUSTOMER);
        User existing = User.builder().id(5L).username("oldName").role(currentRole).build();
        Roles newRole = new Roles(1L, Role.ADMIN);

        when(userRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(roleRepository.getByName(Role.ADMIN)).thenReturn(newRole);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Object request = newUpdateUserRequest("newName", "ADMIN");
        Object result = invokeUpdateUser(5L, request);

        verify(userRepository).save(existing);
        assertEquals("newName", existing.getUsername());
        assertEquals(newRole, existing.getRole());
        assertEquals(5L, extractId(result));
        assertEquals("newName", extractUsername(result));
        assertEquals("ADMIN", extractRole(result));
    }

    @Test
    void updateUser_userNotFound_throws() {
        System.out.println("Running updateUser_userNotFound_throws");
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Object request = newUpdateUserRequest("irrelevant", "ADMIN");
        assertThrows(RuntimeException.class, () -> invokeUpdateUser(99L, request));
    }

    private Class<?> findInnerClass(String simpleName) {
        return List.of(AdminController.class.getDeclaredClasses())
                .stream()
                .filter(c -> c.getSimpleName().equals(simpleName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Inner class not found: " + simpleName));
    }

    private Object newUpdateUserRequest(String username, String role) {
        try {
            Constructor<?> ctor = updateUserRequestClass.getDeclaredConstructor(String.class, String.class);
            ctor.setAccessible(true);
            return ctor.newInstance(username, role);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object invokeUpdateUser(Long id, Object request) {
        try {
            Method method = AdminController.class.getMethod("updateUser", Long.class, updateUserRequestClass);
            method.setAccessible(true);
            return method.invoke(adminController, id, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Long extractId(Object dto) {
        return (Long) invokeAccessor(dto, "id");
    }

    private String extractUsername(Object dto) {
        return (String) invokeAccessor(dto, "username");
    }

    private String extractRole(Object dto) {
        return (String) invokeAccessor(dto, "role");
    }

    private Object invokeAccessor(Object target, String methodName) {
        try {
            Method method = target.getClass().getMethod(methodName);
            method.setAccessible(true);
            return method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

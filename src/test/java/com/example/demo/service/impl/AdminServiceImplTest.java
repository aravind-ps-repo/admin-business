package com.example.demo.service.impl;

import com.example.demo.dto.AdminDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin admin;
    private AdminDTO adminDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        admin = new Admin();
        admin.setId(1L);
        admin.setName("John Doe");
        admin.setEmail("john@example.com");
        admin.setAge(30);
        
        adminDTO = new AdminDTO();
        adminDTO.setName("John Doe");
        adminDTO.setEmail("john@example.com");
        adminDTO.setAge(30);
    }

    @Test
    void testCreateAdmin_Success() {
        when(adminRepository.existsByEmail(adminDTO.getEmail())).thenReturn(false);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        AdminDTO result = adminService.createAdmin(adminDTO);

        assertNotNull(result);
        assertEquals(adminDTO.getName(), result.getName());
        assertEquals(adminDTO.getEmail(), result.getEmail());
        assertEquals(adminDTO.getAge(), result.getAge());
        verify(adminRepository).existsByEmail(adminDTO.getEmail());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testCreateAdmin_DuplicateEmail() {
        when(adminRepository.existsByEmail(adminDTO.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            adminService.createAdmin(adminDTO);
        });
        
        verify(adminRepository).existsByEmail(adminDTO.getEmail());
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void testGetAdminById_Success() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        AdminDTO result = adminService.getAdminById(1L);

        assertNotNull(result);
        assertEquals(admin.getName(), result.getName());
        assertEquals(admin.getEmail(), result.getEmail());
        assertEquals(admin.getAge(), result.getAge());
        verify(adminRepository).findById(1L);
    }

    @Test
    void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.getAdminById(1L);
        });
        
        verify(adminRepository).findById(1L);
    }

    @Test
    void testGetAllAdmins() {
        List<Admin> admins = Arrays.asList(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<AdminDTO> results = adminService.getAllAdmins();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(admin.getName(), results.get(0).getName());
        assertEquals(admin.getEmail(), results.get(0).getEmail());
        assertEquals(admin.getAge(), results.get(0).getAge());
        verify(adminRepository).findAll();
    }

    @Test
    void testUpdateAdmin_Success() {
        AdminDTO updateDTO = new AdminDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setAge(35);

        Admin updatedAdmin = new Admin();
        updatedAdmin.setId(1L);
        updatedAdmin.setName(updateDTO.getName());
        updatedAdmin.setEmail(updateDTO.getEmail());
        updatedAdmin.setAge(updateDTO.getAge());

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(updatedAdmin);

        AdminDTO result = adminService.updateAdmin(1L, updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.getName(), result.getName());
        assertEquals(updateDTO.getEmail(), result.getEmail());
        assertEquals(updateDTO.getAge(), result.getAge());
        verify(adminRepository).findById(1L);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testUpdateAdmin_NotFound() {
        AdminDTO updateDTO = new AdminDTO();
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.updateAdmin(1L, updateDTO);
        });
        
        verify(adminRepository).findById(1L);
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void testDeleteAdmin_Success() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        doNothing().when(adminRepository).deleteById(1L);

        adminService.deleteAdmin(1L);

        verify(adminRepository).existsById(1L);
        verify(adminRepository).deleteById(1L);
    }

    @Test
    void testDeleteAdmin_NotFound() {
        when(adminRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            adminService.deleteAdmin(1L);
        });
        
        verify(adminRepository).existsById(1L);
        verify(adminRepository, never()).deleteById(any());
    }
}
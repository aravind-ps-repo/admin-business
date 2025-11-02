package com.example.demo.controller;

import com.example.demo.dto.AdminDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private AdminDTO adminDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        adminDTO = new AdminDTO();
        adminDTO.setId(1L);
        adminDTO.setName("John Doe");
        adminDTO.setEmail("john@example.com");
        adminDTO.setAge(30);
    }

    @Test
    void testCreateAdmin_Success() {
        when(adminService.createAdmin(any(AdminDTO.class))).thenReturn(adminDTO);

        ResponseEntity<AdminDTO> response = adminController.create(adminDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(adminDTO.getName(), response.getBody().getName());
        assertEquals(adminDTO.getEmail(), response.getBody().getEmail());
        assertEquals(adminDTO.getAge(), response.getBody().getAge());
        verify(adminService).createAdmin(any(AdminDTO.class));
    }

    @Test
    void testCreateAdmin_DuplicateEmail() {
        when(adminService.createAdmin(any(AdminDTO.class)))
                .thenThrow(new DuplicateResourceException("Email already exists"));

        assertThrows(DuplicateResourceException.class, () -> {
            adminController.create(adminDTO);
        });
        
        verify(adminService).createAdmin(any(AdminDTO.class));
    }

    @Test
    void testGetAdminById_Success() {
        when(adminService.getAdminById(1L)).thenReturn(adminDTO);

        ResponseEntity<AdminDTO> response = adminController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(adminDTO.getName(), response.getBody().getName());
        assertEquals(adminDTO.getEmail(), response.getBody().getEmail());
        assertEquals(adminDTO.getAge(), response.getBody().getAge());
        verify(adminService).getAdminById(1L);
    }

    @Test
    void testGetAdminById_NotFound() {
        when(adminService.getAdminById(1L))
                .thenThrow(new ResourceNotFoundException("Admin not found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            adminController.getById(1L);
        });
        
        verify(adminService).getAdminById(1L);
    }

    @Test
    void testGetAllAdmins_Success() {
        List<AdminDTO> admins = Arrays.asList(adminDTO);
        when(adminService.getAllAdmins()).thenReturn(admins);

        ResponseEntity<List<AdminDTO>> response = adminController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(adminDTO.getName(), response.getBody().get(0).getName());
        assertEquals(adminDTO.getEmail(), response.getBody().get(0).getEmail());
        assertEquals(adminDTO.getAge(), response.getBody().get(0).getAge());
        verify(adminService).getAllAdmins();
    }

    @Test
    void testUpdateAdmin_Success() {
        AdminDTO updateDTO = new AdminDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");
        updateDTO.setAge(35);

        when(adminService.updateAdmin(eq(1L), any(AdminDTO.class))).thenReturn(updateDTO);

        ResponseEntity<AdminDTO> response = adminController.update(1L, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updateDTO.getName(), response.getBody().getName());
        assertEquals(updateDTO.getEmail(), response.getBody().getEmail());
        assertEquals(updateDTO.getAge(), response.getBody().getAge());
        verify(adminService).updateAdmin(eq(1L), any(AdminDTO.class));
    }

    @Test
    void testUpdateAdmin_NotFound() {
        AdminDTO updateDTO = new AdminDTO();
        when(adminService.updateAdmin(eq(1L), any(AdminDTO.class)))
                .thenThrow(new ResourceNotFoundException("Admin not found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            adminController.update(1L, updateDTO);
        });
        
        verify(adminService).updateAdmin(eq(1L), any(AdminDTO.class));
    }

    @Test
    void testDeleteAdmin_Success() {
        doNothing().when(adminService).deleteAdmin(1L);

        ResponseEntity<Void> response = adminController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(adminService).deleteAdmin(1L);
    }

    @Test
    void testDeleteAdmin_NotFound() {
        doThrow(new ResourceNotFoundException("Admin not found"))
                .when(adminService).deleteAdmin(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            adminController.delete(1L);
        });
        
        verify(adminService).deleteAdmin(1L);
    }
}
package com.example.demo.controller;

import com.example.demo.dto.AdminDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class AdminControllerBase {

    @Autowired
    private AdminController adminController;

    @MockBean
    private AdminService adminService;

    @BeforeEach
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(adminController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        // Setup common test data
        AdminDTO johnDoe = new AdminDTO();
        johnDoe.setId(1L);
        johnDoe.setName("John Doe");
        johnDoe.setEmail("john@example.com");
        johnDoe.setAge(30);

        AdminDTO janeDoe = new AdminDTO();
        janeDoe.setId(2L);
        janeDoe.setName("Jane Doe");
        janeDoe.setEmail("jane@example.com");
        janeDoe.setAge(25);

        // Setup responses for all possible scenarios
        when(adminService.createAdmin(any(AdminDTO.class)))
                .thenAnswer(invocation -> {
                    AdminDTO input = invocation.getArgument(0);
                    if ("john@example.com".equals(input.getEmail())) {
                        throw new DuplicateResourceException("Email already exists");
                    }
                    input.setId(1L);
                    return input;
                });

        when(adminService.getAdminById(1L)).thenReturn(johnDoe);
        when(adminService.getAdminById(999L))
                .thenThrow(new com.example.demo.exception.ResourceNotFoundException("Admin not found"));

        when(adminService.getAllAdmins()).thenReturn(Arrays.asList(johnDoe, janeDoe));

        when(adminService.updateAdmin(eq(1L), any(AdminDTO.class)))
                .thenAnswer(invocation -> {
                    AdminDTO input = invocation.getArgument(1);
                    input.setId(1L);
                    return input;
                });

        Mockito.doNothing().when(adminService).deleteAdmin(1L);
        Mockito.doThrow(new com.example.demo.exception.ResourceNotFoundException("Admin not found"))
                .when(adminService).deleteAdmin(999L);
    }
}
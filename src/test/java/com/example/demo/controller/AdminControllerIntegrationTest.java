package com.example.demo.controller;

import com.example.demo.DemoApplication;
import com.example.demo.dto.AdminDTO;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Admin testAdmin;

    private static final String TEST_USERNAME = "demo";
    private static final String TEST_PASSWORD = "demo123";

    @BeforeEach
    void setUp() {
        // Clean up database
        adminRepository.deleteAll();

        // Set up test data
        testAdmin = new Admin();
        testAdmin.setName("John Doe");
        testAdmin.setEmail("john@example.com");
        testAdmin.setAge(30);
        testAdmin = adminRepository.save(testAdmin);
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void createAdmin_Success() throws Exception {
        AdminDTO newAdmin = new AdminDTO();
        newAdmin.setName("Jane Doe");
        newAdmin.setEmail("jane@example.com");
        newAdmin.setAge(25);

        mockMvc.perform(post("/api/admins")
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Jane Doe")))
                .andExpect(jsonPath("$.email", is("jane@example.com")))
                .andExpect(jsonPath("$.age", is(25)));
    }

    @Test
    void createAdmin_DuplicateEmail() throws Exception {
        AdminDTO duplicateAdmin = new AdminDTO();
        duplicateAdmin.setName("John Doe 2");
        duplicateAdmin.setEmail("john@example.com"); // Same email as testAdmin
        duplicateAdmin.setAge(35);

        mockMvc.perform(post("/api/admins")
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateAdmin)))
                .andExpect(status().isConflict());
    }

    @Test
    void getAdminById_Success() throws Exception {
        mockMvc.perform(get("/api/admins/{id}", testAdmin.getId())
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.age", is(30)));
    }

    @Test
    void getAdminById_NotFound() throws Exception {
        mockMvc.perform(get("/api/admins/{id}", 999L)
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAdmins_Success() throws Exception {
        // Add another admin
        Admin secondAdmin = new Admin();
        secondAdmin.setName("Jane Doe");
        secondAdmin.setEmail("jane@example.com");
        secondAdmin.setAge(25);
        adminRepository.save(secondAdmin);

        mockMvc.perform(get("/api/admins")
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }

    @Test
    void updateAdmin_Success() throws Exception {
        AdminDTO updateRequest = new AdminDTO();
        updateRequest.setName("John Doe Updated");
        updateRequest.setEmail("john.updated@example.com");
        updateRequest.setAge(31);

        mockMvc.perform(put("/api/admins/{id}", testAdmin.getId())
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@example.com")))
                .andExpect(jsonPath("$.age", is(31)));
    }

    @Test
    void updateAdmin_NotFound() throws Exception {
        AdminDTO updateRequest = new AdminDTO();
        updateRequest.setName("John Doe Updated");
        updateRequest.setEmail("john.updated@example.com");
        updateRequest.setAge(31);

        mockMvc.perform(put("/api/admins/{id}", 999L)
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAdmin_Success() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", testAdmin.getId())
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isNoContent());

        // Verify admin is deleted
        mockMvc.perform(get("/api/admins/{id}", testAdmin.getId())
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAdmin_NotFound() throws Exception {
        mockMvc.perform(delete("/api/admins/{id}", 999L)
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createAdmin_InvalidInput() throws Exception {
        AdminDTO invalidAdmin = new AdminDTO();
        invalidAdmin.setName(""); // Invalid: empty name
        invalidAdmin.setEmail("invalid-email"); // Invalid: not an email format
        invalidAdmin.setAge(-1); // Invalid: negative age

        mockMvc.perform(post("/api/admins")
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAdmin)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAdmin_InvalidInput() throws Exception {
        AdminDTO invalidUpdate = new AdminDTO();
        invalidUpdate.setName(""); // Invalid: empty name
        invalidUpdate.setEmail("invalid-email"); // Invalid: not an email format
        invalidUpdate.setAge(-1); // Invalid: negative age

        mockMvc.perform(put("/api/admins/{id}", testAdmin.getId())
                .with(httpBasic(TEST_USERNAME, TEST_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUpdate)))
                .andExpect(status().isBadRequest());
    }
}
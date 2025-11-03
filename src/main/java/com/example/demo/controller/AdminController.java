package com.example.demo.controller;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.AdminStatisticsDTO;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admins")
@Validated
public class AdminController {
	
	private final AdminService service;

    @Autowired
    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AdminDTO> create(@Valid @RequestBody AdminDTO dto) {
        AdminDTO created = service.createAdmin(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAdminById(id));
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAll() {
        return ResponseEntity.ok(service.getAllAdmins());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable Long id, @Valid @RequestBody AdminDTO dto) {
        return ResponseEntity.ok(service.updateAdmin(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<AdminStatisticsDTO> getStatistics(
            @RequestParam(defaultValue = "30") int seniorAgeThreshold) {
        return ResponseEntity.ok(service.getAdminStatistics(seniorAgeThreshold));
    }
}
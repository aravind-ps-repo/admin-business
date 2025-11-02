package com.example.demo.service.impl;

import com.example.demo.dto.AdminDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository repository;

    public AdminServiceImpl(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        if (repository.existsByEmail(adminDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        Admin admin = AdminMapper.toEntity(adminDTO);
        admin = repository.save(admin);
        return AdminMapper.toDto(admin);
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        return AdminMapper.toDto(admin);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        return repository.findAll().stream()
            .map(AdminMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        Admin admin = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setAge(adminDTO.getAge());
        
        admin = repository.save(admin);
        return AdminMapper.toDto(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found");
        }
        repository.deleteById(id);
    }
}
package com.example.demo.service.impl;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.AdminStatisticsDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.functional.AdminProcessor;
import com.example.demo.functional.AdminValidator;
import com.example.demo.mapper.AdminMapper;
import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final AdminRepository repository;

    // Functional interfaces implementations
    private final AdminValidator emailValidator = (email, age) -> 
        email != null && email.contains("@") && age != null && age > 0;

    private final AdminProcessor<Admin, AdminDTO> customAdminProcessor = admin ->
        new AdminDTO(admin.getId(), admin.getName(), admin.getEmail(), admin.getAge());
    
    private final Predicate<Admin> activeAdminPredicate = admin -> 
        admin.getEmail() != null && admin.getAge() > 0;

    public AdminServiceImpl(AdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        LOGGER.debug("Attempting to create admin with email: {}", adminDTO.getEmail());
        
        // Using custom functional interface for validation
        if (!emailValidator.validate(adminDTO.getEmail(), adminDTO.getAge())) {
            throw new IllegalArgumentException("Invalid admin data");
        }

        if (repository.existsByEmail(adminDTO.getEmail())) {
            LOGGER.warn("Failed to create admin - email already exists: {}", adminDTO.getEmail());
            throw new DuplicateResourceException("Email already exists");
        }

        Admin admin = AdminMapper.toEntity(adminDTO);
        admin = repository.save(admin);
        LOGGER.info("Successfully created admin with ID: {}", admin.getId());
        
        // Using custom AdminProcessor
        return customAdminProcessor.process(admin);
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        LOGGER.debug("Fetching admin with ID: {}", id);
        Admin admin = repository.findById(id)
            .orElseThrow(() -> {
                LOGGER.warn("Admin not found with ID: {}", id);
                return new ResourceNotFoundException("Admin not found");
            });
        LOGGER.debug("Successfully retrieved admin with ID: {}", id);
        return AdminMapper.toDto(admin);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        LOGGER.debug("Fetching all admins");
        
        // Using Predicate and Function
        return repository.findAll().stream()
            .filter(activeAdminPredicate)
            .map(customAdminProcessor::process)
            .collect(Collectors.toList());
    }

    @Override
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        LOGGER.debug("Attempting to update admin with ID: {}", id);
        Admin admin = repository.findById(id)
            .orElseThrow(() -> {
                LOGGER.warn("Failed to update - admin not found with ID: {}", id);
                return new ResourceNotFoundException("Admin not found");
            });
        
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setAge(adminDTO.getAge());
        
        admin = repository.save(admin);
        LOGGER.info("Successfully updated admin with ID: {}", id);
        return AdminMapper.toDto(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        LOGGER.debug("Attempting to delete admin with ID: {}", id);
        if (!repository.existsById(id)) {
            LOGGER.warn("Failed to delete - admin not found with ID: {}", id);
            throw new ResourceNotFoundException("Admin not found");
        }
        repository.deleteById(id);
        LOGGER.info("Successfully deleted admin with ID: {}", id);
    }

    @Override
    public AdminStatisticsDTO getAdminStatistics(int seniorAgeThreshold) {
        LOGGER.debug("Calculating admin statistics with senior age threshold: {}", seniorAgeThreshold);
        
        List<Admin> admins = repository.findAll();
        AdminStatisticsDTO statistics = new AdminStatisticsDTO();
        
        
        Predicate<Admin> isSenior = admin -> admin.getAge() >= seniorAgeThreshold;
        Predicate<Admin> isActive = activeAdminPredicate;
        
        List<String> seniorEmails = admins.stream()
            .filter(isActive.and(isSenior))
            .map(Admin::getEmail)
            .sorted()
            .collect(Collectors.toList());
            
        
        Optional<Admin> youngestAdmin = admins.stream()
            .filter(activeAdminPredicate)
            .min(Comparator.comparingInt(Admin::getAge));
            
        Optional<Admin> oldestAdmin = admins.stream()
            .filter(activeAdminPredicate)
            .max(Comparator.comparingInt(Admin::getAge));
        
       
        statistics.setYoungestAdmin(youngestAdmin.map(Admin::getName).orElse("No admins found"));
        statistics.setOldestAdmin(oldestAdmin.map(Admin::getName).orElse("No admins found"));
        

            
        DoubleSummaryStatistics ageStats = admins.stream()
            .mapToDouble(Admin::getAge)
            .summaryStatistics();
            
        statistics.setAverageAge(ageStats.getAverage());
        statistics.setTotalAdmins((int) ageStats.getCount());
        statistics.setSeniorAdminEmails(seniorEmails);
        
        LOGGER.info("Successfully calculated admin statistics. Total admins: {}", statistics.getTotalAdmins());
        return statistics;
    }

  
}
package com.example.demo.mapper;

import com.example.demo.dto.AdminDTO;
import com.example.demo.model.Admin;

public class AdminMapper {
    public static AdminDTO toDto(Admin admin) {
        if (admin == null) return null;
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setName(admin.getName());
        dto.setEmail(admin.getEmail());
        dto.setAge(admin.getAge());
        return dto;
    }

    public static Admin toEntity(AdminDTO dto) {
        if (dto == null) return null;
        Admin admin = new Admin();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setAge(dto.getAge());
        return admin;
    }
}
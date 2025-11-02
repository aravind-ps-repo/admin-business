package com.example.demo.service;

import com.example.demo.dto.AdminDTO;
import java.util.List;

public interface AdminService {
    AdminDTO createAdmin(AdminDTO adminDTO);
    AdminDTO getAdminById(Long id);
    List<AdminDTO> getAllAdmins();
    AdminDTO updateAdmin(Long id, AdminDTO adminDTO);
    void deleteAdmin(Long id);
}
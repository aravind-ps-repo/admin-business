package com.example.demo.service;

import com.example.demo.dto.AdminDTO;
import com.example.demo.dto.AdminStatisticsDTO;
import java.util.List;
import java.util.Map;

public interface AdminService {
    AdminDTO createAdmin(AdminDTO adminDTO);
    AdminDTO getAdminById(Long id);
    List<AdminDTO> getAllAdmins();
    AdminDTO updateAdmin(Long id, AdminDTO adminDTO);
    void deleteAdmin(Long id);
    AdminStatisticsDTO getAdminStatistics(int seniorAgeThreshold);
}
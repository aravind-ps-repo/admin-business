package com.example.demo.dto;

import java.util.List;

public class AdminStatisticsDTO {
    private double averageAge;
    private int totalAdmins;
    private List<String> seniorAdminEmails;
    private String youngestAdmin;
    private String oldestAdmin;

    // Default constructor
    public AdminStatisticsDTO() {}

    // All args constructor
    public AdminStatisticsDTO(double averageAge, int totalAdmins, List<String> seniorAdminEmails,
                            String youngestAdmin, String oldestAdmin) {
        this.averageAge = averageAge;
        this.totalAdmins = totalAdmins;
        this.seniorAdminEmails = seniorAdminEmails;
        this.youngestAdmin = youngestAdmin;
        this.oldestAdmin = oldestAdmin;
    }

    // Getters and Setters
    public double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public int getTotalAdmins() {
        return totalAdmins;
    }

    public void setTotalAdmins(int totalAdmins) {
        this.totalAdmins = totalAdmins;
    }

    public List<String> getSeniorAdminEmails() {
        return seniorAdminEmails;
    }

    public void setSeniorAdminEmails(List<String> seniorAdminEmails) {
        this.seniorAdminEmails = seniorAdminEmails;
    }

    public String getYoungestAdmin() {
        return youngestAdmin;
    }

    public void setYoungestAdmin(String youngestAdmin) {
        this.youngestAdmin = youngestAdmin;
    }

    public String getOldestAdmin() {
        return oldestAdmin;
    }

    public void setOldestAdmin(String oldestAdmin) {
        this.oldestAdmin = oldestAdmin;
    }

    @Override
    public String toString() {
        return "AdminStatisticsDTO{" +
                "averageAge=" + averageAge +
                ", totalAdmins=" + totalAdmins +
                ", seniorAdminEmails=" + seniorAdminEmails +
                ", youngestAdmin='" + youngestAdmin + '\'' +
                ", oldestAdmin='" + oldestAdmin + '\'' +
                '}';
    }
}
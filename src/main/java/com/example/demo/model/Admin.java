package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be >= 0")
    private Integer age;
    public Admin(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public Admin() {
	}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
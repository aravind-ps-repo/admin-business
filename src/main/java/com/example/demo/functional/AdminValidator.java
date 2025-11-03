package com.example.demo.functional;

@FunctionalInterface
public interface AdminValidator {
    boolean validate(String email, Integer age);
}
package com.example.demo.functional;

@FunctionalInterface
public interface AdminProcessor<T, R> {
    R process(T admin);
}
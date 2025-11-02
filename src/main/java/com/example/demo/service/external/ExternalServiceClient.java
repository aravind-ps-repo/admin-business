package com.example.demo.service.external;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalServiceClient {

    private final RestTemplate restTemplate;

    @Autowired
    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
    public String callExternal() {
        // Example: this URL will fail ; purpose is to show circuit breaker usage.
        String url = "http://localhost:9999/unavailable";
        return restTemplate.getForObject(url, String.class);
    }

    public String fallback(Throwable t) {
        return "external-service-unavailable";
    }
}

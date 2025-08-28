package com.codegym.demo.services;

import com.codegym.demo.models.User;
import com.codegym.demo.repositories.IAuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private IAuthService authService;
    public AuthService(IAuthService authService) {
        this.authService = authService;
    }

    public Optional<User> login(String email, String password) {
        return authService.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }
}

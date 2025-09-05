package com.codegym.demo.controllers;

import com.codegym.demo.models.User;
import com.codegym.demo.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request, Model model) {
        String rememberedEmail = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("rememberEmail".equals(c.getName())) {
                    rememberedEmail = c.getValue();
                }
            }
        }
        model.addAttribute("rememberedEmail", rememberedEmail);
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}

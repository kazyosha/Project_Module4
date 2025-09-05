package com.codegym.demo.controllers;

import com.codegym.demo.models.Product;
import com.codegym.demo.models.User;
import com.codegym.demo.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final ProductService productService;
    private final HttpSession httpSession;

    public HomeController(ProductService productService, HttpSession httpSession) {
        this.productService = productService;
        this.httpSession = httpSession;
    }

    @GetMapping
    public String home(HttpSession session, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        httpSession.setAttribute("email", email);

        List<Product> newProducts = productService.getNewProducts();
        List<Product> featuredProducts = productService.getFeaturedProducts();
        List<Product> saleProducts = productService.getSaleProducts();

//        model.addAttribute("user", currentUser);
        model.addAttribute("newProducts", newProducts);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("saleProducts", saleProducts);
        return "index";
    }
}

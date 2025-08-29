package com.codegym.demo.controllers;

import com.codegym.demo.models.Product;
import com.codegym.demo.models.User;
import com.codegym.demo.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final ProductService productService;
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String home(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:auth/login";
        }
        model.addAttribute("user", currentUser);
        List<Product> newProducts = productService.getNewProducts();
        List<Product> featuredProducts = productService.getFeaturedProducts();
        List<Product> saleProducts = productService.getSaleProducts();

        model.addAttribute("newProducts", newProducts);
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("saleProducts", saleProducts);
        return "index";
    }
}

package com.codegym.demo.controllers;

import com.codegym.demo.dto.AddProductDTO;
import com.codegym.demo.models.Category;
import com.codegym.demo.models.ProductTag;
import com.codegym.demo.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/add")
    public String showFormAddProduct(Model model) {
        AddProductDTO products = new AddProductDTO();
        List<Category> categories = productService.getAllCategories();
        List<ProductTag> tags = productService.getAllTags();

        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("products", products);
        return "admin/add-product";
    }
    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute("products") AddProductDTO products) {
        productService.saveProduct(products);
        return "redirect:/admin/products/add";
    }
}

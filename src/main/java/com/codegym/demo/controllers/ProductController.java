package com.codegym.demo.controllers;

import com.codegym.demo.dto.product.GetAllProduct;
import com.codegym.demo.dto.product.ProductDTO;
import com.codegym.demo.dto.product.UpdateProductDTO;
import com.codegym.demo.models.Category;
import com.codegym.demo.models.ProductTag;
import com.codegym.demo.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/list-product";
    }

    @GetMapping("/products/create")
    public String showFormAddProduct(Model model) {
        GetAllProduct products = new GetAllProduct();
        List<Category> categories = productService.getAllCategories();
        List<ProductTag> tags = productService.getAllTags();

        model.addAttribute("categories", categories);
        model.addAttribute("tags", tags);
        model.addAttribute("products", products);
        return "admin/add-product";
    }

    @PostMapping("/products/create")
    public String addProduct(@ModelAttribute("products") GetAllProduct products) {
        productService.saveProduct(products);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        UpdateProductDTO productDto = productService.getProductForEdit(id);
        model.addAttribute("product", productDto);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("tags", productService.getAllTags());
        return "admin/edit-product";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable("id") long id,
                                @ModelAttribute("product") UpdateProductDTO productDto) {
        productDto.setId(id);
        productService.updateProduct(productDto);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}

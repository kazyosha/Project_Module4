package com.codegym.demo.services;

import com.codegym.demo.dto.product.GetAllProduct;
import com.codegym.demo.dto.product.ProductDTO;
import com.codegym.demo.dto.product.UpdateProductDTO;
import com.codegym.demo.models.Category;
import com.codegym.demo.models.Product;
import com.codegym.demo.models.ProductTag;
import com.codegym.demo.repositories.ICategoryRepository;
import com.codegym.demo.repositories.IProductRepository;
import com.codegym.demo.repositories.IProductTagRepository;
import com.codegym.demo.untils.FileManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private IProductRepository productRepository;
    private ICategoryRepository categoryRepository;
    private IProductTagRepository productTagRepository;
    private final String uploadDir = "F:/uploads/";
    private final FileManager fileManager;

    public ProductService(IProductRepository productRepository,
                          ICategoryRepository categoryRepository,
                          IProductTagRepository productTagRepository, FileManager fileManager) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productTagRepository = productTagRepository;
        this.fileManager = fileManager;
    }

    public List<Product> getNewProducts() {
        return productRepository.findByTagName("New");
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByTagName("Featured");
    }

    public List<Product> getSaleProducts() {
        return productRepository.findByDiscountPriceIsNotNull();
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(p.getId().intValue());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setDiscountPrice(p.getDiscountPrice());
            dto.setStock(p.getStock());
            dto.setImageUrl(p.getImageUrl());

            dto.setCategories(
                    p.getCategories() == null
                            ? new ArrayList<>()
                            : p.getCategories().stream().map(Category::getName).toList()
            );
            return dto;
        }).collect(Collectors.toList());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<ProductTag> getAllTags() {
        return productTagRepository.findAll();
    }

    public void saveProduct(GetAllProduct product) {
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = new BigDecimal(product.getPrice().replaceAll("[^\\d]", ""));
        BigDecimal discountPrice = null;
        if (product.getDiscountPrice() != null && !product.getDiscountPrice().isEmpty()) {
            discountPrice = new BigDecimal(product.getDiscountPrice().replaceAll("[^\\d]", ""));
        }
        Integer stock = product.getStock();

        Product productNew = new Product();
        productNew.setName(name);
        productNew.setDescription(description);
        productNew.setPrice(price);
        productNew.setDiscountPrice(discountPrice);
        productNew.setStock(stock);

        List<Long> categoryId = product.getCategoryIds();
        List<Long> tagId = product.getTagIds();
        MultipartFile file = product.getImage();

        if (!file.isEmpty()) {
            String fileName = fileManager.uploadFile(uploadDir, file);
            System.out.println("Saved file at: " + uploadDir + "/" + fileName);
            productNew.setImageUrl(fileName);
        }

        if (categoryId != null) {
            for (Long idCategory : categoryId) {
                Category category = categoryRepository.findById(idCategory).orElse(null);
                if (category != null) {
                    productNew.getCategories().add(category);
                }
            }
        }
        if (tagId != null) {
            for (Long idTag : tagId) {
                ProductTag tag = productTagRepository.findById(idTag).orElse(null);
                if (tag != null) {
                    productNew.getTags().add(tag);
                }
            }
        }
        productRepository.save(productNew);
    }

    @Transactional(readOnly = true)
    public UpdateProductDTO getProductForEdit(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        UpdateProductDTO dto = new UpdateProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setStock(product.getStock());

        if (product.getCategories() != null) {
            dto.setCategoryIds(
                    product.getCategories().stream()
                            .map(Category::getId)
                            .toList()
            );
        } else {
            dto.setCategoryIds(List.of());
        }

        if (product.getTags() != null) {
            dto.setTagIds(
                    product.getTags().stream()
                            .map(ProductTag::getId)
                            .toList()
            );
        } else {
            dto.setTagIds(List.of());
        }

        return dto;
    }

    @Transactional
    public void updateProduct(UpdateProductDTO dto) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setStock(dto.getStock());

        MultipartFile file = dto.getImage();
        if (!file.isEmpty()) {
            fileManager.deleteFile(uploadDir + "/" + product.getImageUrl());
            String fileName = fileManager.uploadFile(uploadDir, file);
            product.setImageUrl(fileName);
        }

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            product.setCategories(new HashSet<>(categories));
        } else {
            product.setCategories(new HashSet<>());
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<ProductTag> tags = productTagRepository.findAllById(dto.getTagIds());
            product.setTags(new HashSet<>(tags));
        } else {
            product.setTags(new HashSet<>());
        }

        productRepository.save(product);
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
}

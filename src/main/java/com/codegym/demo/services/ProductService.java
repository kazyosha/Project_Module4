package com.codegym.demo.services;

import com.codegym.demo.dto.AddProductDTO;
import com.codegym.demo.models.Category;
import com.codegym.demo.models.Product;
import com.codegym.demo.models.ProductTag;
import com.codegym.demo.repositories.ICategoryRepository;
import com.codegym.demo.repositories.IProductRepository;
import com.codegym.demo.repositories.IProductTagRepository;
import com.codegym.demo.untils.FileManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public List<AddProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<AddProductDTO> result = new ArrayList<>();

        for (Product p : products) {
            AddProductDTO dto = new AddProductDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setDiscountPrice(p.getDiscountPrice());
            dto.setStock(p.getStock());
            // Nếu có quan hệ category, tag thì map thêm
            dto.setCategoryIds(
                    p.getCategories().stream().map(Category::getId).toList()
            );
            dto.setTagIds(
                    p.getTags().stream().map(ProductTag::getId).toList()
            );

            result.add(dto);
        }

        return result;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<ProductTag> getAllTags() {
        return productTagRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById((long) id).orElse(null);
    }

    public void deleteById(int id) {
        productRepository.deleteById((long) id);
    }

    public void updateProduct(int id, Product product) {
        productRepository.save(product);
    }

    public void saveProduct(AddProductDTO product) {
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        BigDecimal discountPrice = product.getDiscountPrice();
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

}

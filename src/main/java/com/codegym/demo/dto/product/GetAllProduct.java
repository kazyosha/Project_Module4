package com.codegym.demo.dto.product;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class GetAllProduct {

    private long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    private MultipartFile image;

    @NotBlank(message = "Giá không được để trống")
    @Pattern(regexp = "^\\d+[\\d,.]*$", message = "Giá không hợp lệ")
    private String price;

    @Pattern(regexp = "^\\d+[\\d,.]*$", message = "Giá khuyến mãi không hợp lệ")
    private String discountPrice;

    @NotNull(message = "Số lượng tồn không được để trống")
    @Min(value = 0, message = "Số lượng tồn phải >= 0")
    private Integer stock;

    private List<Long> categoryIds;
    private List<Long> tagIds;

    public GetAllProduct() {
    }
    public GetAllProduct(String name, String description, MultipartFile image, String price, String discountPrice, Integer stock) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.discountPrice = discountPrice;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}

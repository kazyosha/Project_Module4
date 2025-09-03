package com.codegym.demo.dto.product;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class UpdateProductDTO {
    private long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải > 0")
    private BigDecimal price;

    @Positive(message = "Giá khuyến mãi phải > 0")
    private BigDecimal discountPrice;

    private MultipartFile image;

    @NotNull(message = "Số lượng tồn không được để trống")
    @Min(value = 0, message = "Số lượng tồn phải >= 0")
    private Integer stock;

    private List<Long> categoryIds;
    private List<Long> tagIds;
    public UpdateProductDTO() {}
    public UpdateProductDTO(long id, String name, String description, BigDecimal price, BigDecimal discountPrice, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPrice = discountPrice;
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}

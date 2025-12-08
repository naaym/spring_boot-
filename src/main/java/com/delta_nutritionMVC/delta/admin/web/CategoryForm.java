package com.delta_nutritionMVC.delta.admin.web;

import jakarta.validation.constraints.NotBlank;

public class CategoryForm {

    @NotBlank(message = "Le nom est requis")
    private String name;

    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

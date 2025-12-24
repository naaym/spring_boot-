package com.delta_nutritionMVC.delta.admin.controller;

import com.delta_nutritionMVC.delta.admin.services.CatalogService;
import com.delta_nutritionMVC.delta.admin.web.CategoryForm;
import com.delta_nutritionMVC.delta.admin.web.ProductForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admins")
public class AdminCatalogController {

    private final CatalogService catalogService;

    public AdminCatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        prepareCatalogModel(model);
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String addCategory(@Valid @ModelAttribute("categoryForm") CategoryForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (!bindingResult.hasErrors()) {
            catalogService.createCategory(form);
            return "redirect:/admins/categories";
        }
        prepareCatalogModel(model);
        return "admin/categories";
    }

    @PostMapping("/categories/{id}")
    public String updateCategory(@PathVariable Long id,
                                 String name,
                                 String imageUrl) {
        catalogService.updateCategory(id, name, imageUrl);
        return "redirect:/admins/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        catalogService.deleteCategory(id);
        return "redirect:/admins/categories";
    }

    @PostMapping("/products")
    public String addProduct(@Valid @ModelAttribute("productForm") ProductForm form,
                             BindingResult bindingResult,
                             Model model) {
        if (!bindingResult.hasErrors()) {
            catalogService.createProduct(form);
            return "redirect:/admins/categories";
        }
        prepareCatalogModel(model);
        return "admin/categories";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable String id,
                                @ModelAttribute ProductForm form) {
        catalogService.updateProduct(id, form);
        return "redirect:/admins/categories";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable String id) {
        catalogService.deleteProduct(id);
        return "redirect:/admins/categories";
    }

    private void prepareCatalogModel(Model model) {
        model.addAttribute("categories", catalogService.findAllCategories());
        model.addAttribute("products", catalogService.findAllProducts());
        if (!model.containsAttribute("categoryForm")) {
            model.addAttribute("categoryForm", new CategoryForm());
        }
        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", new ProductForm());
        }
    }
}

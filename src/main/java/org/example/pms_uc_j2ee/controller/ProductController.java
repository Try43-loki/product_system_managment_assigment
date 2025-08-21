package org.example.pms_uc_j2ee.controller;

import jakarta.servlet.http.HttpSession;
import org.example.pms_uc_j2ee.model.Admin;
import org.example.pms_uc_j2ee.model.Product;
import org.example.pms_uc_j2ee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        return "product_index";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add New Product");
        return "product_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            return "product_form";
        } else {
            redirectAttributes.addFlashAttribute("message", "Product with ID " + id + " not found.");
            return "redirect:/products";
        }
    }

    /**
     * UPDATED: Saves a product and automatically assigns the logged-in admin.
     */
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, HttpSession session, RedirectAttributes redirectAttributes) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInUser");
        if (loggedInAdmin == null) {
            // If for some reason no admin is logged in, redirect to login
            return "redirect:/login";
        }

        // Set the admin on the product before saving
        product.setAdmin(loggedInAdmin);
        productRepository.save(product);

        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "The product with ID " + id + " has been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }
}

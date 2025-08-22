package org.example.pms_uc_j2ee.controller;

import org.example.pms_uc_j2ee.model.Product;
import org.example.pms_uc_j2ee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Controller to handle the customer-facing shop page.
 */
@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductRepository productRepository;

    @Autowired
    public ShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showShopPage(Model model) {
        // Fetch all products from the database
        List<Product> productList = productRepository.findAll();

        // Add the product list to the model so it can be accessed in the HTML page
        model.addAttribute("products", productList);

        // Return the view name, which corresponds to 'shop_index.html'
        return "shop_index";
    }
}

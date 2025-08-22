package org.example.pms_uc_j2ee.controller;

import jakarta.servlet.http.HttpSession;
import org.example.pms_uc_j2ee.model.Admin;
import org.example.pms_uc_j2ee.model.Product;
import org.example.pms_uc_j2ee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/account")
public class AdminAccountController {

    private final ProductRepository productRepository;

    @Autowired
    public AdminAccountController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String showAdminAccountPage(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInUser");

        if (loggedInAdmin == null || !(Boolean) session.getAttribute("isAdmin")) {
            return "redirect:/login";
        }

        // Fetch products managed by this admin
        List<Product> products = productRepository.findByAdmin_AdminId(loggedInAdmin.getAdminId());

        model.addAttribute("admin", loggedInAdmin);
        model.addAttribute("products", products);
        model.addAttribute("productCount", products.size());

        return "admin_account_page";
    }
}

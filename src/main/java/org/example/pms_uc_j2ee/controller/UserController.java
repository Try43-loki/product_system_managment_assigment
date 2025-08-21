package org.example.pms_uc_j2ee.controller;

import org.example.pms_uc_j2ee.model.AppUser;
import org.example.pms_uc_j2ee.model.Product;
import org.example.pms_uc_j2ee.repository.ProductRepository;
import org.example.pms_uc_j2ee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for user-facing actions like registration and shopping.
 */
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // --- REGISTRATION ---

    /**
     * Displays the user registration form.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser()); // Binds to th:object="${user}" in the form
        return "register_form"; // Renders templates/register_form.html
    }

    /**
     * Processes the registration form submission.
     */
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") AppUser user, RedirectAttributes redirectAttributes) {
        // In a real application, you would hash the password here.
        // Example: user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}

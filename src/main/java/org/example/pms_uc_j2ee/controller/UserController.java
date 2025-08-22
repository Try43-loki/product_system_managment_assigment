package org.example.pms_uc_j2ee.controller;

import org.example.pms_uc_j2ee.model.AppUser;
import org.example.pms_uc_j2ee.repository.ProductRepository;
import org.example.pms_uc_j2ee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser()); // Binds to th:object="${user}" in the form
        return "register_form"; // Renders templates/register_form.html
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") AppUser user, RedirectAttributes redirectAttributes) {
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}

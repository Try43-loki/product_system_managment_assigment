package org.example.pms_uc_j2ee.controller;


import jakarta.servlet.http.HttpSession;
import org.example.pms_uc_j2ee.model.AppUser;
import org.example.pms_uc_j2ee.model.Order;
import org.example.pms_uc_j2ee.repository.OrderRepository;
import org.example.pms_uc_j2ee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public AccountController(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String showAccountPage(HttpSession session, Model model) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Refresh user data from the database
        userRepository.findById(loggedInUser.getUserId()).ifPresent(user -> {
            List<Order> orders = orderRepository.findByUser_UserIdOrderByOrderDateDesc(user.getUserId());
            model.addAttribute("user", user);
            model.addAttribute("orders", orders);
        });

        return "account_page"; // Renders templates/account_page.html
    }

    @PostMapping("/deposit")
    public String depositFunds(@RequestParam("amount") BigDecimal amount, HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Deposit amount must be positive.");
            return "redirect:/account";
        }

        userRepository.findById(loggedInUser.getUserId()).ifPresent(user -> {
            user.setBalance(user.getBalance().add(amount));
            userRepository.save(user);
            session.setAttribute("loggedInUser", user); // Update session object
            redirectAttributes.addFlashAttribute("successMessage", "Successfully deposited " + amount + " into your account.");
        });

        return "redirect:/account";
    }
}

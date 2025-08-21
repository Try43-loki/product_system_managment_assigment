package org.example.pms_uc_j2ee.controller;

import jakarta.servlet.http.HttpSession;
import org.example.pms_uc_j2ee.model.*;
import org.example.pms_uc_j2ee.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository; // Added AdminRepository

    @Autowired
    public CartController(CartItemRepository cartItemRepository, ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository, AdminRepository adminRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository; // Added AdminRepository
    }

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemRepository.findByUser_UserId(loggedInUser.getUserId());
        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        userRepository.findById(loggedInUser.getUserId()).ifPresent(user -> model.addAttribute("userBalance", user.getBalance()));
        return "cart_view";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemRepository.findByUser_UserId(loggedInUser.getUserId());
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/cart";
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        userRepository.findById(loggedInUser.getUserId()).ifPresent(user -> model.addAttribute("userBalance", user.getBalance()));

        return "checkout_page";
    }


    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") Integer productId, HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            return "redirect:/shop";
        }
        Product product = productOptional.get();

        CartItem cartItem = cartItemRepository.findByUser_UserIdAndProduct_ProductId(loggedInUser.getUserId(), productId);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setUser(loggedInUser);
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
        }

        cartItemRepository.save(cartItem);
        redirectAttributes.addFlashAttribute("successMessage", "Added '" + product.getName() + "' to your cart!");
        return "redirect:/shop";
    }

    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable("id") Integer id, @RequestParam("quantity") int quantity) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            if (quantity > 0) {
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            } else {
                cartItemRepository.delete(cartItem);
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeCartItem(@PathVariable("id") Integer id) {
        cartItemRepository.deleteById(id);
        return "redirect:/cart";
    }

    /**
     * UPDATED: Handles the final checkout process, including transferring funds to the admin.
     */
    @PostMapping("/checkout")
    @Transactional // Ensures all database operations succeed or fail together
    public String processCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        AppUser loggedInUser = (AppUser) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findById(loggedInUser.getUserId()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemRepository.findByUser_UserId(user.getUserId());
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/cart";
        }

        BigDecimal total = cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (user.getBalance().compareTo(total) < 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Insufficient balance.");
            return "redirect:/cart";
        }

        // 1. Create the Order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPricePerUnit(cartItem.getProduct().getPrice());
            order.getOrderItems().add(orderItem);

            // 2. Credit the Admin's account for this item
            Admin productAdmin = cartItem.getProduct().getAdmin();
            if (productAdmin != null) {
                BigDecimal itemTotal = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                productAdmin.setBalance(productAdmin.getBalance().add(itemTotal));
                adminRepository.save(productAdmin);
            }
        }
        orderRepository.save(order);

        // 3. Deduct from user's balance
        user.setBalance(user.getBalance().subtract(total));
        userRepository.save(user);

        session.setAttribute("loggedInUser", user);
        cartItemRepository.deleteAll(cartItems);

        redirectAttributes.addFlashAttribute("successMessage", "Your order has been placed successfully!");
        return "redirect:/home";
    }
}

package org.example.pms_uc_j2ee.controller; // Ensure this package is correct

import jakarta.servlet.http.HttpSession;
import org.example.pms_uc_j2ee.model.Admin;
import org.example.pms_uc_j2ee.model.AppUser;
import org.example.pms_uc_j2ee.repository.AdminRepository;
import org.example.pms_uc_j2ee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class AuthController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Renders templates/login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session) {

        // 1. Check if the user is an Admin
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // IMPORTANT: In a real app, you must compare hashed passwords!
            if (password.equals(admin.getPassword())) {
                session.setAttribute("loggedInUser", admin);
                session.setAttribute("isAdmin", true);
                return "redirect:/home"; // UPDATED: Redirect admins to the home page
            }
        }

        // 2. If not an admin, check if the user is a regular User
        Optional<AppUser> userOptional = userRepository.findByEmail(username); // Assuming login with email for users
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            if (password.equals(user.getPassword())) {
                session.setAttribute("loggedInUser", user);
                session.setAttribute("isAdmin", false);
                return "redirect:/home"; // UPDATED: Redirect users to the home page
            }
        }

        // 3. If no match is found, return to login with an error parameter
        return "redirect:/login?error";
    }


    @GetMapping("/home")
    public String showHomePage(HttpSession session) {
        // Protect the route: if no one is logged in, redirect to login
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "home"; // Renders templates/home.html
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login?logout";
    }
}

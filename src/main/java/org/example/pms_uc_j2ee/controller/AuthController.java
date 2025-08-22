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

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Renders templates/login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session) {

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (password.equals(admin.getPassword())) {
                session.setAttribute("loggedInUser", admin);
                session.setAttribute("isAdmin", true);
                return "redirect:/home";
            }
        }

        Optional<AppUser> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            if (password.equals(user.getPassword())) {
                session.setAttribute("loggedInUser", user);
                session.setAttribute("isAdmin", false);
                return "redirect:/home";
            }
        }

        return "redirect:/login?error";
    }


    @GetMapping("/home")
    public String showHomePage(HttpSession session) {
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

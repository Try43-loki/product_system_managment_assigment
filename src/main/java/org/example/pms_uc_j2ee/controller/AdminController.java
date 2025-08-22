package org.example.pms_uc_j2ee.controller;

import org.example.pms_uc_j2ee.model.Admin;
import org.example.pms_uc_j2ee.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admins") // All handler methods in this class are relative to the /admins path
public class AdminController {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping
    public String listAdmins(Model model) {
        List<Admin> adminList = adminRepository.findAll();
        model.addAttribute("admins", adminList);
        return "index"; // Renders templates/index.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Create a new Admin object to bind form data
        model.addAttribute("admin", new Admin());
        model.addAttribute("pageTitle", "Add New Admin");
        return "admin_form"; // Renders templates/admin_form.html
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute("admin") Admin admin, RedirectAttributes redirectAttributes) {
        adminRepository.save(admin);
        redirectAttributes.addFlashAttribute("message", "The admin has been saved successfully.");
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            model.addAttribute("admin", adminOptional.get());
            model.addAttribute("pageTitle", "Edit Admin (ID: " + id + ")");
            return "admin_form"; // Renders templates/admin_form.html
        } else {
            redirectAttributes.addFlashAttribute("message", "Admin with ID " + id + " not found.");
            return "redirect:/admins";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            adminRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "The admin with ID " + id + " has been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting admin: " + e.getMessage());
        }
        return "redirect:/admins";
    }
}

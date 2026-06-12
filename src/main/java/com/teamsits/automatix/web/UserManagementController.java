package com.teamsits.automatix.web;

import com.teamsits.automatix.enums.UserRole;
import com.teamsits.automatix.service.AppUserService;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/org-admin")
@RequiredArgsConstructor
public class UserManagementController {

    private final AppUserService appUserService;
    private final SecurityUtils securityUtils;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", appUserService.getUsersInCurrentOrg());
        model.addAttribute("roles", new UserRole[]{UserRole.ORG_ADMIN, UserRole.ORG_USER});
        return "org-admin/users";
    }

    @PostMapping("/users")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "ORG_USER") UserRole role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            appUserService.createUser(username, password, fullName, email, role, securityUtils.getCurrentOrganization());
            redirectAttributes.addFlashAttribute("success", "User created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/org-admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appUserService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User removed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/org-admin/users";
    }

    @PostMapping("/users/{id}/toggle-active")
    public String toggleActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appUserService.toggleActive(id);
            redirectAttributes.addFlashAttribute("success", "User status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/org-admin/users";
    }
}

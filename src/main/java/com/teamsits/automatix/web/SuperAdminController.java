package com.teamsits.automatix.web;

import com.teamsits.automatix.entities.Organization;
import com.teamsits.automatix.enums.SubscriptionType;
import com.teamsits.automatix.enums.UserRole;
import com.teamsits.automatix.service.AppUserService;
import com.teamsits.automatix.service.OrganizationService;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final OrganizationService organizationService;
    private final AppUserService appUserService;

    @GetMapping("/organizations")
    public String listOrganizations(Model model) {
        model.addAttribute("organizations", organizationService.getAllOrganizations());
        model.addAttribute("subscriptionTypes", SubscriptionType.values());
        return "admin/organizations";
    }

    @PostMapping("/organizations")
    public String createOrganization(
            @RequestParam String name,
            @RequestParam SubscriptionType subscriptionType,
            @RequestParam(required = false) Integer maxUsers,
            @RequestParam(required = false) String agreementNotes,
            RedirectAttributes redirectAttributes
    ) {
        try {
            organizationService.createOrganization(name, subscriptionType, maxUsers, agreementNotes);
            redirectAttributes.addFlashAttribute("success", "Organization '" + name + "' created.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/organizations";
    }

    @PostMapping("/organizations/{id}/delete")
    public String deactivateOrganization(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            organizationService.deactivateOrganization(id);
            redirectAttributes.addFlashAttribute("success", "Organization deactivated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/organizations";
    }

    @GetMapping("/organizations/{id}/users")
    public String listOrgUsers(@PathVariable Long id, Model model) {
        Organization org = organizationService.getById(id);
        model.addAttribute("org", org);
        model.addAttribute("users", appUserService.getUsersByOrg(org));
        model.addAttribute("roles", new UserRole[]{UserRole.ORG_ADMIN, UserRole.ORG_USER});
        return "admin/organization-users";
    }

    @PostMapping("/organizations/{id}/users")
    public String createOrgUser(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam UserRole role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Organization org = organizationService.getById(id);
            appUserService.createUser(username, password, fullName, email, role, org);
            redirectAttributes.addFlashAttribute("success", "User created.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/organizations/" + id + "/users";
    }

    @PostMapping("/organizations/{orgId}/users/{userId}/delete")
    public String deleteOrgUser(@PathVariable Long orgId, @PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            appUserService.deleteUser(userId);
            redirectAttributes.addFlashAttribute("success", "User removed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/organizations/" + orgId + "/users";
    }

    @PostMapping("/organizations/{orgId}/users/{userId}/toggle-active")
    public String toggleOrgUserActive(@PathVariable Long orgId, @PathVariable Long userId, RedirectAttributes redirectAttributes) {
        try {
            appUserService.toggleActive(userId);
            redirectAttributes.addFlashAttribute("success", "User status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/organizations/" + orgId + "/users";
    }
}

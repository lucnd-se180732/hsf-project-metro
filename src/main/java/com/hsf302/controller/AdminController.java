package com.hsf302.controller;

import com.hsf302.enums.VerificationStatus;
import com.hsf302.pojo.StudentVerification;
import com.hsf302.pojo.User;
import com.hsf302.repository.StudentVerificationRepository;
import com.hsf302.repository.UserRepository;
import com.hsf302.service.interfaces.DashboardService;
import com.hsf302.service.interfaces.StudentVerificationService;
import com.hsf302.service.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private StudentVerificationService verificationService;


    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("totalUsers", dashboardService.countTotalUsers());
        model.addAttribute("pendingVerifications", dashboardService.countPendingVerifications());
        model.addAttribute("verifiedStudents", dashboardService.countVerifiedStudents());
        model.addAttribute("contactRequests", 0);

        return "admin/dashboard";
    }


    @GetMapping("/verifications")
    public String viewVerifications(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String studentCode,
            @RequestParam(required = false) String status,
            Model model) {

        List<StudentVerification> verifications = verificationService.searchVerifications(fullName, studentCode, status);
        model.addAttribute("verifications", verifications);

        return "admin/verifications";
    }


    @PostMapping("/verifications/{id}/approve")
    public String approveStudent(@PathVariable Long id) {
        verificationService.approveVerification(id);
        return "redirect:/admin/verifications";
    }


    @PostMapping("/verifications/{id}/reject")
    public String rejectStudent(@PathVariable Long id) {
        verificationService.rejectVerification(id);
        return "redirect:/admin/verifications";
    }

    @GetMapping("/users")
    public String manageUsers(Model model, HttpServletRequest request) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUri", request.getRequestURI());
        return "admin/manage-users";
    }


    @PostMapping("/toggle-user-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserStatus(id);
        redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái người dùng thành công.");
        return "redirect:/admin/users";
    }


}

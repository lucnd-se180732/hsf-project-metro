package com.hsf302.controller;

import com.hsf302.dto.StudentVerificationDTO;
import com.hsf302.pojo.User;
import com.hsf302.service.interfaces.StudentVerificationService;

import com.hsf302.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private StudentVerificationService studentVerificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/verify-student-form")
    public String showVerifyForm(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) return "redirect:/login";


        User user = userService.getRequiredByEmail(principal.getName());

        if (user == null) return "redirect:/login";

        if (user.isStudent()) {
            model.addAttribute("errorMessage", "Tài khoản đã xác thực thành công!");
            return "user/verify-student-form";
        }

        if (studentVerificationService.hasPendingOrApprovedRequest(user.getId())) {
            model.addAttribute("errorMessage", "Bạn đã gửi đơn.");
            return "user/verify-student-form";
        }

        if (!model.containsAttribute("verificationDTO")) {
            model.addAttribute("verificationDTO", new StudentVerificationDTO());
        }

        return "user/verify-student-form";
    }

    @PostMapping("/verify-student")
    public String handleStudentVerification(
            @ModelAttribute("verificationDTO") StudentVerificationDTO request,
            Principal principal,
            RedirectAttributes redirectAttributes) throws IOException {

        if (principal == null) return "redirect:/login";

        User user = userService.getRequiredByEmail(principal.getName());
        if (user == null) return "redirect:/login";

        if (studentVerificationService.isIdNumberExistsForOther(request.getIdNumber(), user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đã tồn tại trong hệ thống.");
            return "redirect:/verify-student-form";
        }

        if (studentVerificationService.hasPendingOrApprovedRequest(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu của bạn sẽ được xử lí trong 1-2 ngày.");
            return "redirect:/verify-student-form";
        }

        studentVerificationService.handleUpload(request, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Gửi xác minh thành công. Vui lòng chờ xét duyệt.");
        return "redirect:/verify-student-form";
    }


    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        User user = userService.findByEmail(email);
        model.addAttribute("user", user);

        return "user/profile";
    }

}

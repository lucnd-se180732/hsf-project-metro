package com.hsf302.service.impl;

import com.hsf302.enums.Role;
import com.hsf302.pojo.User;
import com.hsf302.repository.UserRepository;
import com.hsf302.service.interfaces.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;


    @Override
    public void handleOAuthLogin(OAuth2User oauthUser, HttpSession session) {
        if (oauthUser == null || session == null) return;

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        if (email == null || name == null) return;

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setRole(Role.CUSTOMER);
            newUser.setStudent(false);
            newUser.setActive(true);
            return userRepository.save(newUser);
        });

        session.setAttribute("user", user);
    }


    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute("user") != null;
    }
}

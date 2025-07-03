package com.hsf302.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {

    void handleOAuthLogin(OAuth2User oauthUser, HttpSession session);
    void logout(HttpSession session);
    boolean isAuthenticated(HttpSession session);
}

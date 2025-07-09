package com.hsf302.security;

import com.hsf302.enums.Role;
import com.hsf302.pojo.User;
import com.hsf302.service.interfaces.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        HttpSession session = request.getSession();

        authService.handleOAuthLogin(oauthUser, session);

        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login?error");
            return;
        }

        if (!user.isActive()) {
            SecurityContextHolder.clearContext();
            session.invalidate();
            response.sendRedirect("/login?error=disabled");
            return;
        }

        CustomOAuth2User customUser = new CustomOAuth2User(
                oauthUser,
                List.of(new SimpleGrantedAuthority(user.getRole().name())) // "ADMIN", "CUSTOMER"
        );

        Authentication customAuth = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(customAuth);

        session.setAttribute("fullName", user.getFullName());
        session.setAttribute("role", user.getRole().name());
        session.setAttribute("student", user.isStudent());

        if (user.getRole() == Role.ADMIN) {
            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/home");
        }
    }
}

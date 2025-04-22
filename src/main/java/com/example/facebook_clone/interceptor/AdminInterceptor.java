package com.example.facebook_clone.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.facebook_clone.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User currentUser = (User) session.getAttribute("currentUser");

            if (currentUser != null && currentUser.getRole() == User.Role.admin) {
                return true; // Cho phép truy cập nếu là admin
            }
        }

        response.sendRedirect("/home?error=unauthorized"); // Chuyển hướng nếu không phải admin
        return false;
    }
}

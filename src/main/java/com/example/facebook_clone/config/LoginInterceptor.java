package com.example.facebook_clone.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

//Bạn sẽ dùng nó để kiểm tra xem người dùng đã đăng nhập chưa 
//(tức là đã có trong session chưa).
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false); // false để không tạo mới nếu chưa có
        if (session != null && session.getAttribute("currentUser") != null) {
            return true; // đã đăng nhập
        }

        response.sendRedirect("/login"); // chưa đăng nhập thì chuyển về login
        return false;
    }
}
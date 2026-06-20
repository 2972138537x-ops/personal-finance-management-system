package com.study.usermanagement.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.usermanagement.common.Result;
import com.study.usermanagement.dao.UserDao;
import com.study.usermanagement.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            writeResult(response, new Result(false, "请先登录", null));
            return false;
        }

        User currentUser = userDao.findByToken(token);

        if (currentUser == null) {
            writeResult(response, new Result(false, "登录状态无效，请重新登录", null));
            return false;
        }
        String uri = request.getRequestURI();
        if (uri.startsWith("/admin") && !"ADMIN".equals(currentUser.getRole())) {
            writeResult(response, new Result(false, "没有管理员权限", null));
            return false;
        }
        request.setAttribute("currentUser", currentUser);
        return true;
    }

    private void writeResult(HttpServletResponse response, Result result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

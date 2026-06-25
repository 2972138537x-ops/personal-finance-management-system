package com.study.usermanagement.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.usermanagement.common.Result;
import com.study.usermanagement.entity.User;
import com.study.usermanagement.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
// 登录拦截器：统一检查 token，并限制管理员接口只能由 ADMIN 访问
// ログインインターセプター：token を共通チェックし、管理者APIは ADMIN のみに制限する
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    // 请求进入 Controller 前先执行：校验 token，并判断 /admin 接口是否需要管理员权限
    // Controller 実行前に token を検証し、/admin API では管理者権限も確認する
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头读取前端传来的登录 token
        // リクエストヘッダーからフロントが送ったログイン token を取得する
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            writeResult(response, new Result(false, "请先登录", null));
            return false;
        }

        // 根据 token 查询当前登录用户，查不到就说明登录状态失效
        // token で現在ログイン中のユーザーを検索し、見つからなければログイン状態は無効
        User currentUser = userMapper.findByToken(token);

        if (currentUser == null) {
            writeResult(response, new Result(false, "登录状态无效，请重新登录", null));
            return false;
        }
        String uri = request.getRequestURI();
        // /admin 开头的接口必须是 ADMIN 角色才能访问
        // /admin から始まるAPIは ADMIN ロールだけアクセスできる
        if (uri.startsWith("/admin") && !"ADMIN".equals(currentUser.getRole())) {
            writeResult(response, new Result(false, "没有管理员权限", null));
            return false;
        }
        // 把当前用户放进 request，后续 Controller 可以直接取出使用
        // 現在ユーザーを request に保存し、後続の Controller で利用できるようにする
        request.setAttribute("currentUser", currentUser);
        return true;
    }

    // 拦截器不能直接 return Result，所以手动把 Result 转成 JSON 写入响应
    // インターセプターでは Result を直接返せないため、JSON に変換して response に書き込む
    private void writeResult(HttpServletResponse response, Result result) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

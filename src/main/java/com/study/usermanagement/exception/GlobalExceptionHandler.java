package com.study.usermanagement.exception;

import com.study.usermanagement.common.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理 @Valid 校验失败的异常，并把错误信息包装成统一的 Result 返回
    // @Valid のバリデーション失敗を処理し、統一形式の Result として返す
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //e.getBindingResult() 获取这次参数绑定和校验的结果。哪个字段错了,为什么错了,错误提示是什么,前端传了什么不合法数据
        //e.getBindingResult().getFieldError() 获取第一个字段错误。
        //e.getBindingResult().getFieldError().getDefaultMessage() 获取你在注解里写的 message@NotBlank(message = "用户名不能为空")
        if (e.getBindingResult().getFieldError() != null) {
            String message = e.getBindingResult().getFieldError().getDefaultMessage();
            return new Result(false, message, null);
        }
        return new Result(false, "参数校验失败", null);
    }

    // 处理请求参数缺失，例如 /transaction-stats/month 没传 year 或 month
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = "缺少请求参数：" + e.getParameterName();
        return new Result(false, message, null);
    }

    // 处理参数类型错误，例如 month=abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "参数类型错误：" + e.getName();
        return new Result(false, message, null);
    }

    // 处理 JSON 格式错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new Result(false, "请求体 JSON 格式错误", null);
    }

    // 处理业务运行时异常
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return new Result(false, e.getMessage(), null);
    }

    // 兜底异常处理
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return new Result(false, "服务器内部错误", null);
    }


}

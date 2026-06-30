package com.study.usermanagement.exception;

import com.study.usermanagement.common.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
// 全局异常处理器：把常见异常统一转换成 Result 返回给前端
// グローバル例外ハンドラー：よくある例外を共通の Result に変換してフロントへ返す
public class GlobalExceptionHandler {

    // 处理 @Valid 校验失败的异常，并把错误信息包装成统一的 Result 返回
    // @Valid のバリデーション失敗を処理し、統一形式の Result として返す
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // e.getBindingResult() 获取这次参数绑定和校验的结果
        // e.getBindingResult() は今回のパラメータバインドとバリデーション結果を取得する
        // getFieldError() 获取第一个字段错误，getDefaultMessage() 获取注解里的 message
        // getFieldError() は最初のフィールドエラー、getDefaultMessage() はアノテーションの message を取得する
        if (e.getBindingResult().getFieldError() != null) {
            String message = e.getBindingResult().getFieldError().getDefaultMessage();
            return new Result(false, message, null);
        }
        return new Result(false, "参数校验失败", null);
    }

    // 处理请求参数缺失，例如 /transaction-stats/month 没传 year 或 month
    // 必須リクエストパラメータ不足を処理する。例：year や month が未指定
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String message = "缺少请求参数：" + e.getParameterName();
        return new Result(false, message, null);
    }

    // 处理参数类型错误，例如 month=abc
    // パラメータ型エラーを処理する。例：month=abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String message = "参数类型错误：" + e.getName();
        return new Result(false, message, null);
    }

    // 处理 JSON 格式错误
    // JSON 形式エラーを処理する
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new Result(false, "请求体 JSON 格式错误", null);
    }

    // Service 抛出 BusinessException 后，由这里统一转换成 Result 返回给前端
    // BusinessException が発生した場合は、Result 形式でフロントエンドに返す
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return new Result(false, e.getMessage(), null);
    }

    // 处理未预期的运行时异常，避免把系统错误细节直接返回给前端
    // 想定外の実行時例外を処理し、詳細をフロントへ直接返さない
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        return new Result(false, "服务器内部错误", null);
    }

    // 兜底异常处理
    // 最後の保険としてその他の例外を処理する
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return new Result(false, "服务器内部错误", null);
    }



}

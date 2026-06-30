package com.study.usermanagement.exception;

// 业务异常：Service 层遇到业务条件不成立时主动抛出
// 業務例外：Service 層で業務条件を満たさない場合に明示的に投げる
public class BusinessException extends RuntimeException {

    // message 会交给 RuntimeException 保存，之后可通过 e.getMessage() 取出
    // message は RuntimeException に保存され、後で e.getMessage() で取得できる
    public BusinessException(String message){
        super(message);
    }

}

package com.study.usermanagement.dto;

// AI 请求体：接收前端传来的用户问题
// AI リクエストボディ：フロントから送られたユーザーの質問を受け取る
public class AiRequest {

    // 用户输入的问题文本
    // ユーザーが入力した質問テキスト
    private String message;

    public AiRequest() {
    }

    public AiRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.study.usermanagement.common;

// 统一返回结果：所有接口都用 success、message、data 返回给前端
// 共通レスポンス：すべてのAPIは success、message、data の形式でフロントへ返す
public class Result {
    private boolean success;
    private String message;
    private Object data;

    // 无参构造方法：给框架或工具创建对象使用
    public Result() {
    }

    // 有参构造方法：快速创建统一返回结果
    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 获取成功状态；boolean 的 getter 通常叫 isSuccess
    public boolean isSuccess() {
        return success;
    }

    // 设置成功状态
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 获取返回信息
    public String getMessage() {
        return message;
    }

    // 设置返回信息
    public void setMessage(String message) {
        this.message = message;
    }

    // 获取返回数据
    public Object getData() {
        return data;
    }

    // 设置返回数据
    public void setData(Object data) {
        this.data = data;
    }

    // 把 Result 对象转成字符串，主要方便调试查看
    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

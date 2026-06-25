package com.study.usermanagement.common;

// 统一返回结果：所有接口都用 success、message、data 返回给前端
// 共通レスポンス：すべてのAPIは success、message、data の形式でフロントへ返す
public class Result {
    // 是否成功：true 表示成功，false 表示失败
    // 成功したかどうか：true は成功、false は失敗
    private boolean success;

    // 返回给前端看的提示信息
    // フロントに表示するメッセージ
    private String message;

    // 返回给前端的数据，可以是对象、列表或 null
    // フロントに返すデータ。オブジェクト、リスト、null の場合がある
    private Object data;

    // 无参构造方法：给框架或工具创建对象使用
    // デフォルトコンストラクタ：フレームワークやツールがオブジェクトを作るときに使う
    public Result() {
    }

    // 有参构造方法：快速创建统一返回结果
    // 引数付きコンストラクタ：共通レスポンスを素早く作成する
    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 获取成功状态；boolean 的 getter 通常叫 isSuccess
    // 成功状態を取得する。boolean の getter は通常 isSuccess という名前にする
    public boolean isSuccess() {
        return success;
    }

    // 设置成功状态
    // 成功状態をセットする
    public void setSuccess(boolean success) {
        this.success = success;
    }

    // 获取返回信息
    // レスポンスメッセージを取得する
    public String getMessage() {
        return message;
    }

    // 设置返回信息
    // レスポンスメッセージをセットする
    public void setMessage(String message) {
        this.message = message;
    }

    // 获取返回数据
    // レスポンスデータを取得する
    public Object getData() {
        return data;
    }

    // 设置返回数据
    // レスポンスデータをセットする
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

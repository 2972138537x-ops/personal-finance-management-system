package com.study.usermanagement.service;

import com.study.usermanagement.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiService {

    // 从 application.properties 读取 OpenRouter API Key
    // application.properties から OpenRouter API キーを読み込む
    @Value("${openrouter.api-key}")
    private String apiKey;

    // 从 application.properties 读取 OpenRouter API 请求地址
    // application.properties から OpenRouter API のリクエストURLを読み込む
    @Value("${openrouter.api-url}")
    private String apiUrl;

    // 从 application.properties 读取要使用的模型
    // application.properties から使用するモデル名を読み込む
    @Value("${openrouter.model}")
    private String model;

    /**
     * 调用 OpenRouter API，返回统一 Result
     * OpenRouter API を呼び出して、統一された Result を返す
     */
    public Result chat(String message) {
        // 1. 判断用户输入是否为空
        // 1. ユーザー入力が空かどうかをチェックする
        if (message == null || message.trim().isEmpty()) {
            return new Result(false, "请输入问题。", null);
        }

        // 2. 检查 OpenRouter API Key 是否读取成功
        // 2. OpenRouter API キーが正しく読み込まれているか確認する
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("${")) {
            return new Result(false, "OpenRouter API Key 未正确配置，请检查 OPENROUTER_API_KEY 环境变量。", null);
        }

        // 3. 检查 API 地址是否配置
        // 3. API URL が設定されているか確認する
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            return new Result(false, "OpenRouter API 地址未配置，请检查 application.properties。", null);
        }

        // 4. 检查模型名是否配置
        // 4. モデル名が設定されているか確認する
        if (model == null || model.trim().isEmpty()) {
            return new Result(false, "OpenRouter 模型未配置，请检查 openrouter.model。", null);
        }

        try {
            // 5. 创建 RestTemplate，用来发送 HTTP 请求
            // 5. HTTP リクエストを送信するための RestTemplate を作成する
            RestTemplate restTemplate = new RestTemplate();

            // 6. 设置请求头
            // 6. リクエストヘッダーを設定する
            HttpHeaders headers = new HttpHeaders();

            // 告诉 OpenRouter：这次发送的是 JSON 数据
            // OpenRouter に「送信するデータは JSON 形式です」と伝える
            headers.setContentType(MediaType.APPLICATION_JSON);

            // OpenRouter 使用 Bearer Token 认证
            // OpenRouter は Bearer Token 認証を使う
            headers.setBearerAuth(apiKey);

            // 可选请求头：用于标识你的项目来源，不影响接口功能
            // 任意ヘッダー：プロジェクトの識別用。機能には影響しない
            headers.set("HTTP-Referer", "http://localhost:8080");
            headers.set("X-Title", "Personal Finance Management System");

            // 7. 创建请求体，也就是要发送给 OpenRouter 的 JSON 数据
            // 7. OpenRouter に送る JSON データ、つまりリクエストボディを作成する
            Map<String, Object> requestBody = buildRequestBody(message);

            // 8. 把请求体和请求头打包成一个完整请求
            // 8. リクエストボディとヘッダーをまとめて、完全なリクエストにする
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 9. 真正发送 POST 请求给 OpenRouter API
            // 9. OpenRouter API に POST リクエストを送信する
            ResponseEntity<Map> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 10. 解析 OpenRouter 返回的 JSON，取出 AI 回复文本
            // 10. OpenRouter から返ってきた JSON を解析し、AI の回答テキストを取り出す
            String answer = parseOpenRouterResponse(response.getBody());

            if (isAiErrorMessage(answer)) {
                return new Result(false, answer, null);
            }

            return new Result(true, "AI回复成功", answer);

        } catch (HttpClientErrorException.TooManyRequests e) {
            // 429：请求过于频繁，免费模型也有限速
            // 429：リクエストが多すぎる。無料モデルにもレート制限がある
            return new Result(false, "AI 服务请求过于频繁，请稍后再试。", null);

        } catch (HttpClientErrorException.Unauthorized e) {
            // 401：API Key 错误或没有权限
            // 401：API キーが間違っている、または権限がない
            return new Result(false, "OpenRouter API Key 无效，请检查 OPENROUTER_API_KEY 是否设置正确。", null);

        } catch (HttpClientErrorException.Forbidden e) {
            // 403：当前 API Key 或账号没有权限调用模型
            // 403：現在の API キーまたはアカウントにモデルを呼び出す権限がない
            return new Result(false, "AI 服务没有访问权限，请检查 OpenRouter API Key 或模型权限。", null);

        } catch (HttpClientErrorException.NotFound e) {
            // 404：模型名不存在，或者 API 地址错误
            // 404：モデル名が存在しない、または API URL が間違っている
            return new Result(false, "AI 模型不存在，请检查 openrouter.model 是否正确。", null);

        } catch (HttpClientErrorException.BadRequest e) {
            // 400：请求格式错误，可能是模型名或请求体结构有问题
            // 400：リクエスト形式エラー。モデル名やリクエストボディの形式が原因の可能性がある
            return new Result(false, "AI 请求格式有误，请检查 OpenRouter API 地址、模型名和请求体格式。", null);

        } catch (ResourceAccessException e) {
            // 网络连接失败，例如网络断开、访问 OpenRouter 失败
            // ネットワーク接続失敗。ネットが切れている、または OpenRouter にアクセスできない場合
            return new Result(false, "无法连接 AI 服务，请检查网络后再试。", null);

        } catch (Exception e) {
            // 其他未知错误，避免把一大段英文异常直接返回给前端
            // その他の未知エラー。長い英語の例外をそのまま画面に返さないようにする
            e.printStackTrace();
            return new Result(false, "AI 服务暂时不可用，请稍后再试。", null);
        }
    }

    /**
     * 组装发送给 OpenRouter 的请求体
     * OpenRouter に送るリクエストボディを作成する
     */
    private Map<String, Object> buildRequestBody(String message) {
        Map<String, Object> requestBody = new HashMap<>();

        // OpenRouter 的 Chat Completions 格式接近 OpenAI：model + messages
        // OpenRouter の Chat Completions 形式は OpenAI に近い：model + messages
        requestBody.put("model", model);

        List<Map<String, String>> messages = new ArrayList<>();

        // system：告诉 AI 它在这个项目里的角色
        // system：このプロジェクト内での AI の役割を伝える
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是个人财务管理系统中的 AI 助手。请用中文回答用户关于记账、收入、支出、消费分析、省钱建议的问题。回答要简洁、实用，不要太长。");

        // user：用户真正输入的问题
        // user：ユーザーが実際に入力した質問
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", message);

        messages.add(systemMessage);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        return requestBody;
    }

    /**
     * 解析 OpenRouter 返回的数据
     * OpenRouter から返されたデータを解析する
     */
    private String parseOpenRouterResponse(Map body) {
        if (body == null) {
            return "AI服务没有返回内容。";
        }

        // OpenRouter 返回结果通常在 choices 里面
        // OpenRouter の回答は通常 choices の中に入っている
        Object choicesObj = body.get("choices");

        if (!(choicesObj instanceof List)) {
            return "AI返回格式异常：没有 choices。";
        }

        List choices = (List) choicesObj;

        if (choices.isEmpty()) {
            return "AI没有生成回答。";
        }

        // 取第一个回答
        // 最初の回答を取得する
        Object firstObj = choices.get(0);

        if (!(firstObj instanceof Map)) {
            return "AI返回格式异常：choice 不是对象。";
        }

        Map first = (Map) firstObj;

        // 取 message
        // message を取得する
        Object messageObj = first.get("message");

        if (!(messageObj instanceof Map)) {
            return "AI返回格式异常：没有 message。";
        }

        Map message = (Map) messageObj;

        // 最后取 content，这就是 AI 的回答
        // 最後に content を取得する。これが AI の回答
        Object contentObj = message.get("content");

        if (contentObj == null) {
            return "AI没有返回文本内容。";
        }

        return contentObj.toString();
    }

    /**
     * 判断 AI 返回内容是否是错误提示
     * AI の返却内容がエラーメッセージかどうかを判断する
     */
    private boolean isAiErrorMessage(String answer) {
        if (answer == null) {
            return true;
        }

        return answer.startsWith("AI服务")
                || answer.startsWith("AI 服务")
                || answer.startsWith("AI返回")
                || answer.startsWith("AI没有")
                || answer.startsWith("无法连接")
                || answer.startsWith("OpenRouter API Key");
    }
}

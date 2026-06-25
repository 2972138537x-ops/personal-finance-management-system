package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.AiRequest;
import com.study.usermanagement.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
// AI 控制器：接收前端问题，把调用外部 AI 的工作交给 AiService
// AI コントローラー：フロントの質問を受け取り、外部 AI 呼び出しは AiService に任せる
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * AI 聊天接口
     * AI チャット用のインターフェース
     */
    @Tag(name = "AI助手", description = "用户通过自然语言获取记账、消费分析和省钱建议")
    @Operation(summary = "AI聊天", description = "接收用户问题，调用外部大语言模型 API 返回财务建议")
    @PostMapping("/chat")
    public Result chat(@RequestBody AiRequest aiRequest) {
        // Controller 只负责接收请求，真正调用 AI 的逻辑交给 Service
        // Controller はリクエストを受け取るだけで、AI 呼び出しの処理は Service に任せる
        return aiService.chat(aiRequest.getMessage());
    }
}

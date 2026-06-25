package com.study.usermanagement.controller;

import com.study.usermanagement.common.Result;
import com.study.usermanagement.dto.AiRequest;
import com.study.usermanagement.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
     * AI 聊天接口
     * AI チャット用のインターフェース
     */
    @PostMapping("/chat")
    public Result chat(@RequestBody AiRequest aiRequest) {
        // Controller 只负责接收请求，真正调用 AI 的逻辑交给 Service
        // Controller はリクエストを受け取るだけで、AI 呼び出しの処理は Service に任せる
        return aiService.chat(aiRequest.getMessage());
    }
}

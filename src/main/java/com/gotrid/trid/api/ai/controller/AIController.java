package com.gotrid.trid.api.ai.controller;


import com.gotrid.trid.api.ai.service.AIService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/ai")
@Tag(name = "AI", description = "Endpoints for AI-related functionalities")
@SecurityRequirement(name = "Bearer Authentication")
public class AIController {

    private final AIService aiService;

/*
    private final String SYSTEM_MESSAGE =
            """
            You are an AI shopping assistant for an e-commerce platform.

            Instructions:
            1. Provide specific, helpful recommendations with reasons
            2. Be conversational and friendly
            3. If recommending products, explain why they would be a good fit
            4. Keep responses concise but informative
            """;
*/

    @PostMapping("/respond")
    public ResponseEntity<String> suggestRecipe(
            @RequestParam(name = "message", defaultValue = "Tell me more about yourself")
            String message
    ) {
        return ResponseEntity.ok(aiService.getAssistantReply(message));
    }
}

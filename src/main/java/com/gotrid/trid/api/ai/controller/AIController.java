package com.gotrid.trid.api.ai.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI", description = "Endpoints for AI-related functionalities")
@SecurityRequirement(name = "Bearer Authentication")
public class AIController {
    private final ChatClient chatClient;

    private final List<Message> conversation;
    private final String SYSTEM_MESSAGE = """
            Suggest seafood recipe.
            If someone asks about something else
            just say 'I can only suggest seafood recipes.'
            """;

    public AIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();

        this.conversation = new ArrayList<>();
        conversation.add(new SystemMessage(SYSTEM_MESSAGE));
    }

    @GetMapping
    public String suggestRecipe(
            @RequestParam(name = "message", defaultValue = "Tell me more about yourself")
            String message
    ) {
        final Message userMessage = new UserMessage(message);
        conversation.add(userMessage);

        String modelResponse = chatClient.prompt()
                .messages(conversation)
                .call()
                .content();

        if (modelResponse != null) {
            conversation.add(new AssistantMessage(modelResponse));
        }

        return modelResponse;
    }

    @DeleteMapping
    public void clearConversation() {
        conversation.clear();
        conversation.add(new SystemMessage(SYSTEM_MESSAGE));
    }
}

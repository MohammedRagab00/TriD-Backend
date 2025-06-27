package com.gotrid.trid.api.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String chat(String prompt) {
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

}

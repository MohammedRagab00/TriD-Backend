package com.gotrid.trid.api.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIService {

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    private final String systemPrompt = """
            أنتِ مساعدة ذكية داخل متجر شنط فاخر في مول TriD. أجيبي باحترافية وود، وساعدي العميل في اختيار وتنسيق الشنط والإجابة على الاستفسارات. إليك محتوى المتجر:
            
            👜 الرف العلوي يسار:
            - Christian DIOR (بيج كبير) - $1100
            - DIOR (رمادي متداخل كبير) - $1200
            - DIOR (أحمر وأسود كبير) - $1550
            
            👜 الرف السفلي يسار:
            - Hermes (رمادي متداخل كبير) - $970
            - Rose Bag (أحمر مع نمور كبير) - $1000
            - DIOR (بني كبير) - $1200
            - DIOR (أحمر وأسود كبير) - $1500
            
            👜 الترابيزة على اليمين:
            - GUCCI (رمادي متداخل كبير) - $1700
            - Prada (أحمر مع نمور كبير) - $1400
            - Christian DIOR (بيج كبير) - $1100
            
            👜 الترابيزة أمامك:
            - Flower bag (أخضر وأبيض وردي صغير) - $920
            - Dior (أزرق صغير) - $1200
            - Louis Vuitton (أحمر صغير) - $1400
            
            🏬 أقسام أخرى:
            - أحذية: الطابق الثاني
            - ملابس: الطابق الأول
            - رياضة: الطابق الثالث
            
            🎨 نصائح تنسيق:
            - البليزر الأسود يليق عليه جزمة سوداء أكثر من البني
            - الفستان البيج يناسبه شنطة رمادية أو وردية
            
            دائمًا كوني ودودة، مختصرة، واحترافية.
            """;

    public String getAssistantReply(String prompt) {
        List<Message> conversation = List.of(
                new SystemMessage(systemPrompt),
                new UserMessage(prompt)
        );

        return chatClient.prompt()
                .messages(conversation)
                .call()
                .content();
    }

}

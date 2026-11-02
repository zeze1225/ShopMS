package com.example.shoppingcart.aicodehelper.ai;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperTest {
    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void chat() {
        aiCodeHelper.chat("nihao");
    }


    @Test
    void chatwithMessage() {
        UserMessage userMessage=UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://img-s.msn.cn/tenant/amp/entityid/AA2g7Sdr.img?w=768&h=531&m=6&x=336&y=114&s=620&d=279")
        );
        aiCodeHelper.chatwithMessage(userMessage);
    }

    @Test
    void testChat() {
    }

    @Test
    void testChatwithMessage() {
    }

    @Test
    void testChat1() {
    }

    @Test
    void testChatwithMessage1() {
    }
}
package com.ssup.backend.domain.user.recommend.test;

import com.ssup.backend.domain.user.recommend.OpenAiClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AiTestController {

    private final OpenAiClient openAiClient;

    public AiTestController(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @GetMapping("/embedding")
    public int test() {
        return openAiClient.embed("I love backend development").size();
    }
}

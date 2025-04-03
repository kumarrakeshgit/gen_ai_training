package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.dalle.ImageGenerator;
import com.epam.training.gen.ai.service.gpt.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/openAI/")
public class OpenAiController {

    @Autowired
    ImageGenerator imageGenerator;

    @Autowired
    ChatService chatService;

    @GetMapping("/image/generation")
    public String imageGenerator(@RequestParam String prompt) throws IOException, InterruptedException {
            return imageGenerator.generateImage(prompt);
    }

    @GetMapping("/chat/completion")
    public String chatCompletion(@RequestParam String prompt){
       return chatService.getChatCompletions(prompt).toString();
    }

    /*
    user prompt like below as we are using light Model
    1. Turn on light 2
    2. Please turn on the lamp
    3. Turn on light 2 and light 3
    */
    @GetMapping("/chat/plugin")
    public String chatHistory(@RequestParam String prompt){
        return chatService.getChatHistory(prompt).toString();
    }

}

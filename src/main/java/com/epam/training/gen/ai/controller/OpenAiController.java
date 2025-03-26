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

}

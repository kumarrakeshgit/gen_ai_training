package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.ChatResponse;
import com.epam.training.gen.ai.service.ChatServiceReg;
import com.epam.training.gen.ai.service.DataIngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatServiceReg chatService;

    @Autowired
    DataIngestionService dataIngestionService;

    @PostMapping
    //@CrossOrigin(origins = "http://localhost:3000")
    public ChatResponse processMsg(@RequestBody ChatRequest chatRequest) {
        System.out.println(chatRequest.getUserMsg());
        var aiMessage = chatService.rag(chatRequest);
        var response = ChatResponse.builder().aiMsg(aiMessage).build();
        return response;
    }

    @GetMapping
    public String welcome() {
        return "Welcome to Chat Bot";
    }

    @PostMapping("/setup")
    public void processMsg() {
        dataIngestionService.setupRagChatbot();
    }

}
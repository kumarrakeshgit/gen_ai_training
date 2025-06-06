package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.history.SimpleKernelHistory;
import com.epam.training.gen.ai.service.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/azure/ai")
public class AIController {

    @Autowired
    SimplePromptService simplePromptService;

    @Autowired
    SimpleKernelHistory simpleKernelHistory;

    @GetMapping("/query")
    public String getResponse(@RequestParam String input){
        return simplePromptService.getChatCompletionsHistory(input).toString();
    }

    @GetMapping("/context/query")
    public String getResponseWithContext(@RequestParam String input){
        return simpleKernelHistory.processWithHistory(input);
    }

}

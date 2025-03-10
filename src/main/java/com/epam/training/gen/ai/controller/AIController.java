package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.promt.SimplePromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/azure/ai")
public class AIController {

    @Autowired
    SimplePromptService simplePromptService;
    @GetMapping("/query")
    public String getResponse(@RequestParam String input){
        return simplePromptService.getChatCompletions(input).toString();
    }

}

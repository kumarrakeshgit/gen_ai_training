package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceReg {

  @Autowired
  AdvancedRagService advancedRagService;

  public String rag(ChatRequest chatRequest) {
    return advancedRagService.generateAnswer(chatRequest);
  }
}
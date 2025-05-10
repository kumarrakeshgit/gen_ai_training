package com.epam.training.gen.ai.model;

import lombok.Data;

@Data
public class ChatRequest {
  private String userMsg;
  private boolean newChatThread;
}
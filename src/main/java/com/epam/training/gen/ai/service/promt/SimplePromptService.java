package com.epam.training.gen.ai.service.promt;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for generating chat completions using Azure OpenAI.
 * <p>
 * This service interacts with the Azure OpenAI API to generate chat completions
 * based on a static greeting message. It retrieves responses from the AI model
 * and logs them.
 */
@Slf4j
@Service
public class SimplePromptService {

    @Value("${client-openai-deployment-name}")
    private String DEPLOYMENT_OR_MODEL_NAME;

    @Autowired
    OpenAIAsyncClient openAIAsyncClient;


    public List<String> getChatCompletions(String chatInput) {
        ChatRequestUserMessage chatRequestUserMessage = new ChatRequestUserMessage(chatInput);
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(List.of(chatRequestUserMessage));

        var completions = openAIAsyncClient
                .getChatCompletions(DEPLOYMENT_OR_MODEL_NAME, chatCompletionsOptions)
                .block();

        return completions != null ? completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .toList() : null;
    }
}

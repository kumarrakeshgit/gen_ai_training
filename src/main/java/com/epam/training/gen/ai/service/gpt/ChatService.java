package com.epam.training.gen.ai.service.gpt;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.constants.ModelService;
import com.epam.training.gen.ai.constants.OpenAiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    OpenAIAsyncClient openAIAsyncClient;

    @Autowired
    ModelService modelService;

    public List<String> getChatCompletions(String prompt) {
        ChatRequestUserMessage chatRequestUserMessage = new ChatRequestUserMessage(prompt);
        ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(List.of(chatRequestUserMessage));
        var completions = openAIAsyncClient
                .getChatCompletions(modelService.selectModel(OpenAiModel.CHAT_COMPL_GPT_35), chatCompletionsOptions)
                .block();
        return completions != null ? completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .toList() : null;
    }
}

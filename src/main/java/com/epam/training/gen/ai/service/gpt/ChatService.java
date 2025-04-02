package com.epam.training.gen.ai.service.gpt;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.constants.ModelService;
import com.epam.training.gen.ai.constants.OpenAiModel;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    OpenAIAsyncClient openAIAsyncClient;

    @Autowired
    ModelService modelService;

    @Autowired
    ChatCompletionService chatCompletionService;

    @Autowired
    Kernel kernel;

    @Autowired
    InvocationContext invocationContext;

    public List<String> getChatCompletions(String prompt) {
        ChatHistory history = new ChatHistory();
        history.addUserMessage(prompt);

        List<ChatMessageContent<?>> response = chatCompletionService.getChatMessageContentsAsync(
                history, kernel, invocationContext).block();

        history.addAll(response);
        return response != null ? response.stream().map(ChatMessageContent::getContent).toList() : null;
    }
}

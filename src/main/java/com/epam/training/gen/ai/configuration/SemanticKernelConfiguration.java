package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.constants.ModelService;
import com.epam.training.gen.ai.constants.OpenAiModel;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuration class for setting up Semantic Kernel components.
 * <p>
 * This configuration provides several beans necessary for the interaction with
 * Azure OpenAI services and the creation of kernel plugins. It defines beans for
 * chat completion services, kernel plugins, kernel instance, invocation context,
 * and prompt execution settings.
 */
@Configuration
public class SemanticKernelConfiguration {

    @Autowired
    private ModelService modelService;

    @Value("${temperature}")
    private String temperature;

    /**
     * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
     *
     * @param openAIAsyncClient the {@link OpenAIAsyncClient} to communicate with Azure OpenAI
     * @return an instance of {@link ChatCompletionService}
     */
    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(modelService.selectModel(OpenAiModel.CHAT_COMPL_GPT_35))
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

    /**
     * Creates a {@link Kernel} bean to manage AI services and plugins.
     *
     * @param chatCompletionService the {@link ChatCompletionService} for handling completions
     * @return an instance of {@link Kernel}
     */
    @Bean
    public Kernel kernel(ChatCompletionService chatCompletionService) {
        return Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
    }

    /**
     * Creates an {@link InvocationContext} bean with default prompt execution settings.
     *
     * @return an instance of {@link InvocationContext}
     */
    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withPromptExecutionSettings(promptExecutionsSettingsMap().get(modelService.selectModel(OpenAiModel.CHAT_COMPL_GPT_35)))
                .build();
    }

    /**
     * Creates a map of {@link PromptExecutionSettings} for different models.
     *
     * @return a map of model names to {@link PromptExecutionSettings}
     */
    @Bean
    public Map<String, PromptExecutionSettings> promptExecutionsSettingsMap() {
        return Map.of(modelService.selectModel(OpenAiModel.CHAT_COMPL_GPT_35), PromptExecutionSettings.builder()
                .withTemperature(Double.parseDouble(temperature))
                .build());
    }
}


package com.epam.training.gen.ai.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Value("${model-deployment-gpt-35-turbo}")
    private String model_deployment_gpt_35_turbo;
    @Value("${model-deployment-gpt-4-turbo}")
    private String model_deployment_gpt_4_turbo;
    @Value("${model-deployment-dall}")
    private String model_deployment_dall;
    @Value("${model-deployment-Mixtral-8x7B-Instruct-v0.1}")
    private String model_deployment_mixtral_instruct;


    public String selectModel(OpenAiModel service) {
        return switch (service) {
            case IMAGE_GEN_DALL_E -> model_deployment_dall;
            case CHAT_COMPL_GPT_35 -> model_deployment_gpt_35_turbo;
            case CHAT_COMPT_MIXTRAL_INSTRUCT -> model_deployment_mixtral_instruct;
            case CHAT_COMPL_GPT_4 -> model_deployment_gpt_4_turbo;
        };
    }
}

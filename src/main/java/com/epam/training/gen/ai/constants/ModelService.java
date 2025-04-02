package com.epam.training.gen.ai.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Value("${client-openai-deployment-name}")
    private String model_deployment_gpt_35_turbo;
    @Value("${model-deployment-gpt-4-turbo}")
    private String model_deployment_gpt_4_turbo;
    @Value("${model-deployment-dall}")
    private String model_deployment_dall;
    @Value("${model-deployment-Mixtral-8x7B-Instruct-v0.1}")
    private String model_deployment_mixtral_instruct;


    public String selectModel(OpenAiModel service) {
        return switch (service) {
            case DALL_E_3 -> model_deployment_dall;
            case GPT_35_TURBO -> model_deployment_gpt_35_turbo;
            case MIXTRAL_8X7B_INSTRUCT_V0_1 -> model_deployment_mixtral_instruct;
            case GPT_4_TURBO -> model_deployment_gpt_4_turbo;
        };
    }

    public static OpenAiModel getModelNameEnum(String modelName){
        return OpenAiModel.valueOf(modelName.toUpperCase().replace("-","_").replace(".","_"));
    }
}

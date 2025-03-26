package com.epam.training.gen.ai.service.dalle;

import com.epam.training.gen.ai.constants.ModelService;
import com.epam.training.gen.ai.constants.OpenAiModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.epam.training.gen.ai.constants.ModelService.getModelNameEnum;

@Service
public class ImageGenerator {

    @Value("${client-openai-endpoint}")
    private String API_URL;
    // possible to use Stability AI(stability.stable-diffusion-xl, stability.sd3-large-v1, stability.stable-image-ultra-v1), OpenAI(dall-e-3)

    @Value("${client-openai-key}")
    private String API_KEY;

    @Value("${client-openai-deployment-name}")
    private String deployed_model;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelService modelService;

    public String generateImage(String prompt) throws IOException, InterruptedException {
        var requestBody = new HashMap<>();
        requestBody.put("messages", new Object[]{Map.of("role", "user", "content", prompt)});
        requestBody.put("max_tokens", 1000);

        var requestJson = objectMapper.writeValueAsString(requestBody);

        String model = modelService.selectModel(getModelNameEnum(deployed_model));
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://ai-proxy.lab.epam.com/openai/deployments/"+model))
                .header("Content-Type", "application/json")
                .header("Api-Key", API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            var jsonResponse = objectMapper.readTree(response.body());
            return jsonResponse.at("/choices/0/message/custom_content/attachments/1/url").asText();
        } else {
            throw new RuntimeException("Failed to generate image: " + response.body());
        }
    }
}

package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.dalle.ImageGenerator;
import com.epam.training.gen.ai.service.gpt.ChatService;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import com.google.protobuf.Descriptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/openAI/")
public class OpenAiController {

    @Autowired
    ImageGenerator imageGenerator;

    @Autowired
    ChatService chatService;

    @Autowired
    SimpleVectorActions simpleVectorActions;

    @GetMapping("/image/generation")
    public String imageGenerator(@RequestParam String prompt) throws IOException, InterruptedException {
            return imageGenerator.generateImage(prompt);
    }

    @GetMapping("/chat/completion")
    public String chatCompletion(@RequestParam String prompt){
       return chatService.getChatCompletions(prompt).toString();
    }

    /*
    user prompt like below as we are using light Model
    1. Turn on light 2
    2. Please turn on the lamp
    3. Turn on light 2 and light 3
    */
    @GetMapping("/chat/plugin")
    public String chatHistory(@RequestParam String prompt){
        return chatService.getChatHistory(prompt).toString();
    }

    @GetMapping("/qdrant/search")
    public String grpcApiSearch(@RequestParam String text) throws ExecutionException, InterruptedException {
        return simpleVectorActions.search(text).toString();
    }

    @GetMapping("/qdrant/process")
    public ResponseEntity<String> processAndSave(@RequestParam String text) throws ExecutionException, InterruptedException {
        simpleVectorActions.processAndSaveText(text);
        return ResponseEntity.ok().body("processed");
    }

    @GetMapping("/qdrant/collection/{collection_name}")
    public ResponseEntity<String> createCollection(@PathVariable String collection_name) throws ExecutionException, InterruptedException {
        simpleVectorActions.createCollection(collection_name);
        return ResponseEntity.ok().body("collection created");
    }

    @GetMapping("/qdrant/collections")
    public ResponseEntity<List<String>> createCollection() throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(simpleVectorActions.listCollection());
    }

    @GetMapping("/qdrant/embedding")
    public ResponseEntity<ArrayList<Float>> buildEmbedding(@RequestParam String text) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok().body(simpleVectorActions.processTextAndRetrieveVector(text));
    }

}

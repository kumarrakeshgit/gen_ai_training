package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.ChatRequest;
import com.epam.training.gen.ai.model.QuestionAnsweringAgent;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.AugmentationRequest;
import dev.langchain4j.rag.AugmentationResult;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import dev.langchain4j.rag.query.Metadata;

@Component
public class AdvancedRagService extends RagService {

  QuestionAnsweringAgent agent;

  @PostConstruct
  public void init() {
    agent = advancedQuestionAnsweringAgent();
  }

  public String generateAnswer(ChatRequest chatRequest) {
    if (chatRequest.isNewChatThread()) {
      agent = advancedQuestionAnsweringAgent();
    }
    return agent.answer(chatRequest.getUserMsg());
  }

  public String retrieveAnswer(ChatRequest chatRequest) {
    return getChatResult(chatRequest.getUserMsg());
  }

  private QuestionAnsweringAgent advancedQuestionAnsweringAgent() {
    ChatLanguageModel chatModel = getChatModel();

    // Chat memory to remember previous interactions
    ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

    ContentRetriever contentRetriever = getEmbeddingStoreContentRetriever();

    PromptTemplate promptTemplate = PromptTemplate.from(
        "You are a question answering bot. You will be given a QUESTION and a set of paragraphs in the CONTENT section. You need to answer the question using the text present in the CONTENT section."
        + "If the answer is not present in the CONTENT text then reply: `I don't have answer of this question` \n"
        + "CONTENT: " + "{{contents}}" + "\n" + "QUESTION: " + "{{userMessage}}" + "\n");

    ContentInjector contentInjector = DefaultContentInjector.builder()
         .promptTemplate(promptTemplate)
        .build();

    RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
        .contentRetriever(contentRetriever)
        .contentInjector(contentInjector)
        .build();

    return AiServices.builder(QuestionAnsweringAgent.class)
        .chatLanguageModel(chatModel)
        .retrievalAugmentor(retrievalAugmentor)
        .chatMemory(chatMemory)
        .build();
  }

  private String getChatResult(String userQuery){
    ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
    UserMessage userMessage = UserMessage.from(userQuery);
    Metadata metadata = Metadata.from(userMessage, chatMemory.id(), chatMemory.messages());

    ContentRetriever contentRetriever = getEmbeddingStoreContentRetriever();

    PromptTemplate promptTemplate = PromptTemplate.from(
            "You are a question answering bot. You will be given a QUESTION and a set of paragraphs in the CONTENT section. You need to answer the question using the text present in the CONTENT section."
                    + "If the answer is not present in the CONTENT text then reply: `I don't have answer of this question` \n"
                    + "CONTENT: " + "{{contents}}" + "\n" + "QUESTION: " + "{{userMessage}}" + "\n");

    ContentInjector contentInjector = DefaultContentInjector.builder()
            .promptTemplate(promptTemplate)
            .build();

    RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
            .contentRetriever(contentRetriever)
            .contentInjector(contentInjector)
            .build();

    AugmentationRequest augmentationRequest = new AugmentationRequest(userMessage, metadata);

    AugmentationResult augmentationResult = retrievalAugmentor.augment(augmentationRequest);
    ChatMessage chatMessage = augmentationResult.chatMessage();
    chatMemory.add(chatMessage);
    return chatMessage.toString();
  }
}
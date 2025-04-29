package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.constants.OpenAiModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class RagService {

  @Autowired
  QdrantClient qdrantClient;

  @Value("${client-openai-key}")
  String API_KEY;

  protected ContentRetriever getEmbeddingStoreContentRetriever() {
    EmbeddingStore<TextSegment> embeddingStore =
        QdrantEmbeddingStore.builder()
            .collectionName("world_history_collection")
                .client(qdrantClient)
            .build();

    EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder().apiKey(API_KEY).build();

    return EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(embeddingModel)
        .maxResults(2)
        .minScore(0.6)
        .build();
  }

  protected ChatLanguageModel getChatModel() {
    return OpenAiChatModel.builder()
        .apiKey(API_KEY)
        .modelName(OpenAiModel.GPT_35_TURBO.name())
        .build();
  }
}
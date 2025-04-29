package com.epam.training.gen.ai.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections.Distance;
import io.qdrant.client.grpc.Collections.VectorParams;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DataIngestionService {

  @Autowired
  QdrantClient qdrantClient;

  @Value("${client-openai-key}")
  String API_KEY;

  public void setupRagChatbot() {
    insertDocuments();
  }

  /*private QdrantClient getQdrantClient() {
    // Authentication Ref: https://qdrant.tech/documentation/cloud/quickstart-cloud/
    return new QdrantClient(
            QdrantGrpcClient.newBuilder(
                            ChatConstants.QDRANT_GRPC_HOST,
                            ChatConstants.QDRANT_GRPC_PORT,
                            true)
                    .withApiKey(ChatConstants.QDRANT_API_KEY)
                    .build());
  }*/

  private void createCollection() {
    try {
      qdrantClient.createCollectionAsync("world_history_collection",
              VectorParams.newBuilder().setDistance(Distance.Dot).setSize(1536)
                      .build()).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private void insertDocuments() {
    EmbeddingModel embeddingModel = getEmbeddingModel();
    DocumentSplitter documentSplitter = DocumentSplitters.recursive(1000, 150);
    String fileContent = getFileContent();
    Document doc = Document.from(fileContent, Metadata.from("document-type", "history-document"));

    EmbeddingStore<TextSegment> embeddingStore = getEmbeddingStore();
    List<TextSegment> segments = documentSplitter.split(doc);
    Response<List<Embedding>> embeddingResponse = embeddingModel.embedAll(segments);
    List<Embedding> embeddings = embeddingResponse.content();
    embeddingStore.addAll(embeddings, segments);

  }

  private EmbeddingModel getEmbeddingModel() {
    return OpenAiEmbeddingModel.builder().apiKey(API_KEY).build();
  }

  private EmbeddingStore<TextSegment> getEmbeddingStore() {
    // Ref: https://qdrant.tech/documentation/frameworks/langchain4j/
    return QdrantEmbeddingStore.builder()
            .collectionName("world_history_collection")
            .host("localhost")
            .port(6334)
            .build();
  }

  /**
   * Read the data from the file
   */
  private String getFileContent() {
    Resource companyDataResource = new ClassPathResource("data/data.txt");
    try {
      File file = companyDataResource.getFile();
        return new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
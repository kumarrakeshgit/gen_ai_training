package com.epam.training.gen.ai.configuration;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutionException;

/**
 * Configuration class for setting up the Qdrant Client.
 * <p>
 * This configuration defines a bean that provides a client for interacting
 * with a Qdrant service. The client is built using gRPC to connect to a
 * Qdrant instance running at the specified host and port.
 */

@Slf4j
@Configuration
public class QdrantConfiguration {

    /**
     * Creates a {@link QdrantClient} bean for interacting with the Qdrant service.
     *
     * @return an instance of {@link QdrantClient}
     */
    @Bean
    public QdrantClient qdrantClient() throws ExecutionException, InterruptedException {
        return new QdrantClient(QdrantGrpcClient.newBuilder("localhost", 6334, false).build());
    }
}

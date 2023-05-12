package it.tai.notificationmanagement.server.config;

import it.tai.notificationmanagement.service.DynamoMessageHistoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoMessageHistoryRepository buildDbService(DynamoDbClient client){
        return new DynamoMessageHistoryRepository(client);
    }

    @Bean
    @ConditionalOnProperty(prefix = "application.dynamodb", name = "mode", havingValue = "accesskey", matchIfMissing = true)
    public DynamoDbClient buildDynamoDbClient(ApplicationProperties properties) {
        return DynamoDbClient.builder().region(properties.getDynamodb().getRegion())
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials
                                .create(properties.getDynamodb().getAccesskey(),properties.getDynamodb().getSecretkey()))).build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "application.dynamodb", name = "mode", havingValue = "direct", matchIfMissing = false)
    public DynamoDbClient buildDynamoDbClientLambda() {
        return DynamoDbClient.create();
    }
}

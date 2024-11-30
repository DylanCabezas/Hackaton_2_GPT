package dbp.proyecto.gpt.config;

import com.azure.ai.openai.ChatCompletionsClient;
import com.azure.ai.openai.ChatCompletionsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureClientConfig {

    @Value("${azure.ai.endpoint}")
    private String endpoint;

    @Value("${azure.ai.key}")
    private String key;

    @Bean
    public ChatCompletionsClient chatCompletionsClient() {
        return new ChatCompletionsClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildClient();
    }
}
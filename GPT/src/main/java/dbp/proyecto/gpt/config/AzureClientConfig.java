package dbp.proyecto.gpt.config;

import com.azure.ai.openai.ChatCompletionsClient;
import com.azure.ai.openai.ChatCompletionsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureClientConfig {

    @Bean
    public ChatCompletionsClient chatCompletionsClient() {
        return new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential("TU_API_KEY"))  // Reemplaza con tu clave
                .endpoint("https://tu-recurso.openai.azure.com")  // Reemplaza con tu endpoint
                .buildClient();
    }
}
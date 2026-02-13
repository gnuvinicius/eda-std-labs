package br.dev.garage474.mspedido.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de beans gerais da aplicação.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Registra o ObjectMapper como um Bean.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}


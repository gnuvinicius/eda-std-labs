package br.dev.garage474.config;

import br.dev.garage474.service.CustomerServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.xml.ws.Endpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class SoapEndpointPublisher {

    private static final Logger log = LoggerFactory.getLogger(SoapEndpointPublisher.class);
    private Endpoint endpoint;

    @Inject
    private CustomerServiceImpl customerService;

    @PostConstruct
    public void publishEndpoint() {
        try {
            String address = "http://0.0.0.0:8080/ws/customer";
            endpoint = Endpoint.publish(address, customerService);
            log.info("servico SOAP publicado em: {}", address);
        } catch (Exception e) {
            log.error("erro ao publicar endpoint SOAP: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PreDestroy
    public void stopEndpoint() {
        if (endpoint != null && endpoint.isPublished()) {
            endpoint.stop();
            log.info("servico SOAP parado");
        }
    }
}

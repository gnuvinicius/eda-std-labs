package br.dev.garage474.dto;

import br.dev.garage474.utils.LocalDateTimeAdapter;
import br.dev.garage474.entity.Customer;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerDto {

    private static final Logger log = LoggerFactory.getLogger(CustomerDto.class);

    @XmlElement
    private UUID id;

    @XmlElement
    private String name;

    @XmlElement
    private String email;

    @XmlElement
    private UUID tenantId;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime createdAt;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime updatedAt;

    public static CustomerDto mapToDto(Customer saved) {
        log.info("Customer to CustomerDto: {}", saved);
        return CustomerDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .tenantId(saved.getTenantId())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }
}

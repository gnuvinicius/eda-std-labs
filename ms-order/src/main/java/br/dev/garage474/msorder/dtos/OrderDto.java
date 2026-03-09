package br.dev.garage474.msorder.dtos;

import java.util.UUID;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "orderDto", namespace = "http://service.garage474.dev.br/")
@XmlType(name = "orderDto", namespace = "http://service.garage474.dev.br/", propOrder = {
    "id", "customerId", "productId", "quantity"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDto {
    
    @XmlElement
    private UUID id;
    @XmlElement
    private String customerId;
    @XmlElement
    private String productId;
    @XmlElement
    private int quantity;
    
}

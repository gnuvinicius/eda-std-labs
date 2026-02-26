package br.dev.garage474.msorder.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateOrderDto {

    @XmlElement
    private String customerId;
    @XmlElement
    private String productId;
    @XmlElement
    private int quantity;
    
}

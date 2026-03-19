package br.dev.garage474.msorder.dtos;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * DTO representing an address used for shipping.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "addressDto", namespace = "https://service.garage474.dev.br/")
@XmlType(name = "addressDto", namespace = "https://service.garage474.dev.br/", propOrder = {
        "id", "street", "number", "complement", "neighborhood", "city", "state", "zipCode", "country"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressDto {

    @XmlElement
    private Long id;

    @XmlElement
    private String street;

    @XmlElement
    private String number;

    @XmlElement
    private String complement;

    @XmlElement
    private String neighborhood;

    @XmlElement
    private String city;

    @XmlElement
    private String state;

    @XmlElement
    private String zipCode;

    @XmlElement
    private String country;
}


package br.dev.garage474.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "GetAllCustomersResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAllCustomersResponseDto", namespace = "http://dto.garage474.dev.br/")
public class GetAllCustomersResponse {

    @XmlElement(name = "customers")
    private List<CustomerDto> customers;

}

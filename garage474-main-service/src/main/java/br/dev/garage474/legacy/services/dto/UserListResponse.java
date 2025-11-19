package br.dev.garage474.legacy.services.dto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserListResponse {
    
    private List<UserDTO> result;
    private Integer total;
    private Integer page;
    private Integer perPage;

    public UserListResponse() {}

    public List<UserDTO> getResult() {
        return result;
    }

    public UserListResponse(List<UserDTO> result, Integer total, Integer page, Integer perPage) {
        this.result = result;
        this.total = total;
        this.page = page;
        this.perPage = perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
}

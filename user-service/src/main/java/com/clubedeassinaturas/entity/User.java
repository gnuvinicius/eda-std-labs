package com.clubedeassinaturas.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends PanacheEntity {

    private String name;
    private String email;
    private boolean active = true;

    protected User() {
    }

    public static User of(String name, String email) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.persist();
        return user;
    }

    public void updateStatus(boolean isActive) {
        this.active = isActive;
        this.persist();
    }

    public String toJson() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

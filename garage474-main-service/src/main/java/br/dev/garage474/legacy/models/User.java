package br.dev.garage474.legacy.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    private String name;
    private String email;
    private String cpf;
    private LocalDate birthDate;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    protected User() {}

    public static User of(String name, String email, String cpf, LocalDate birthDate, String phone) {
        User user = new User();
        user.name = name;
        user.email = email;
        user.cpf = cpf;
        user.birthDate = birthDate;
        user.phone = phone;
        user.active = true;
        return user;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getCpf() { return this.cpf; }

    public LocalDate getBirthDate() { return this.birthDate; }

    public String getPhone() { return this.phone; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public LocalDateTime getUpdatedAt() { return this.updatedAt; }
}

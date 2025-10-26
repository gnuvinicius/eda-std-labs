package br.dev.garage474.legacy.models;

import jakarta.persistence.*;

@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String cpf;

    protected User() {}

    public static User of(String name, String cpf) {
        User user = new User();
        user.name = name;
        user.cpf = cpf;
        return user;
    }

    public String getName() {
        return this.name;
    }

    public String getCpf() {
        return this.cpf;
    }
}

package com.khai.quizguru.model.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.RefreshToken;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private RefreshToken refreshToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Quiz> quizzes;
}

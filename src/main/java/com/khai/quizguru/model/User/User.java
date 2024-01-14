package com.khai.quizguru.model.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.RefreshToken;
import com.khai.quizguru.model.UserDateAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "users")
public class User extends UserDateAudit {

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

    @OneToMany
    @JsonIgnore
    private List<Record> records;
}

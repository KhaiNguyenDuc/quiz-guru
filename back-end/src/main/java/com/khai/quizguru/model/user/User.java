package com.khai.quizguru.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.*;
import com.khai.quizguru.model.Record;
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

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "is_enabled")
    private Boolean isEnable;

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

    @OneToOne
    @JoinColumn(name = "library_id", referencedColumnName = "id")
    @JsonIgnore
    private Library library;


    @OneToOne
    @JoinColumn(name="image_id",referencedColumnName = "id")
    private Image image;


    @OneToOne
    @JoinColumn(name="background_image_id",referencedColumnName = "id")
    private Image backgoundImage;

}

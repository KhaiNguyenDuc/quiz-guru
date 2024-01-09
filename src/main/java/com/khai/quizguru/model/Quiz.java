package com.khai.quizguru.model;

import com.khai.quizguru.model.User.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Lob
    @Column(name = "given_text", columnDefinition = "LONGTEXT")
    private String givenText;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
    private List<Question> questions;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

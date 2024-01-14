package com.khai.quizguru.model;

import com.khai.quizguru.model.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "quizzes")
public class Quiz extends UserDateAudit{

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

    @Column(name = "type")
    private String type;

    @Column(name = "number")
    private Integer number;

    @Column(name = "language")
    private String language;

    @Column(name = "level")
    private String level;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<Record> records;
}

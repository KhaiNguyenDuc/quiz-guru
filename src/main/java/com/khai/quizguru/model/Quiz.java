package com.khai.quizguru.model;

import com.khai.quizguru.enums.Level;
import com.khai.quizguru.enums.QuizType;
import com.khai.quizguru.model.question.Question;
import com.khai.quizguru.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuizType type;

    @Column(name = "number")
    private Integer number;

    @Column(name = "language")
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<Record> records;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}

package com.khai.quizguru.model.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.enums.QuestionType;
import com.khai.quizguru.model.Choice;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.RecordItem;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "query")
    private String query;

    @OneToMany(mappedBy = "question")
    private List<Choice> choices;

    @Column(name = "explanation")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuestionType type;

    @Transient
    @JsonIgnore
    private List<Integer> answers; // Not stored in the database but used for processing

    public void setAnswer(Integer answer) {
        this.choices.get(answer).setIsCorrect(true);
    }

    @ManyToOne
    @JoinColumn(name="quiz_id",referencedColumnName = "id")
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<RecordItem> recordItems;
}
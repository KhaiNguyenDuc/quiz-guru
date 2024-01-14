package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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

    @Transient
    @JsonIgnore
    private int answer; // Not stored in the database but used for processing

    public void setAnswer(int answer) {
        this.answer = answer;
        // Set isCorrect for each choice based on the answer index
        for (int i = 0; i < choices.size(); i++) {
            choices.get(i).setIsCorrect(i == answer);
        }
    }

    @ManyToOne
    @JoinColumn(name="quiz_id",referencedColumnName = "id")
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "question")
    @JsonIgnore
    private List<RecordItem> recordItems;
}
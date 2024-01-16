package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.question.Question;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "choices")
@NoArgsConstructor
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name="question_id",referencedColumnName = "id")
    private Question question;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    @ManyToMany
    @JoinTable(
            name = "record_item_choice",
            joinColumns = @JoinColumn(name = "choice_id"),
            inverseJoinColumns = @JoinColumn(name = "record_item_id")
    )
    @JsonIgnore
    private List<RecordItem> recordItems;

}
package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "record_item")
public class RecordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "record_item_choice",
            joinColumns = @JoinColumn(name = "record_item_id"),
            inverseJoinColumns = @JoinColumn(name = "choice_id")
    )
    private List<Choice> selectedChoices;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    @JsonIgnore
    private Record record;

}

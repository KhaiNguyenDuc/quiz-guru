package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "record_item")
public class RecordItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_choice_id", referencedColumnName = "id")
    private Choice selectedChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", referencedColumnName = "id")
    @JsonIgnore
    private Record record;

}

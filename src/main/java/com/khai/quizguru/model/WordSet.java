package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "word_sets")
public class WordSet extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "library_id", referencedColumnName = "id")
    @JsonIgnore
    private Library library;

    @OneToMany(mappedBy = "wordSet")
    private List<Word> words;

    @Column(name = "word_number")
    private Integer wordNumber;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    private Integer reviewNumber;

}

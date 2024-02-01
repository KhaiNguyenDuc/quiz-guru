package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "definition", columnDefinition = "LONGTEXT")
    private String definition;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "word_set_id", referencedColumnName = "id")
    @JsonIgnore
    private WordSet wordSet;


}

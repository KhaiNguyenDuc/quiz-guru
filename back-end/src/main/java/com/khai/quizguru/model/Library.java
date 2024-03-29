package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@Table(name = "library")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(mappedBy = "library")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "library")
    private List<WordSet> wordSets;
}

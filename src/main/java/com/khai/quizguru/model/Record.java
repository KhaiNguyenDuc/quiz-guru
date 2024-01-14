package com.khai.quizguru.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khai.quizguru.model.User.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "records")
public class Record extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "record", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RecordItem> recordItems;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(name = "score")
    private Integer score;
}

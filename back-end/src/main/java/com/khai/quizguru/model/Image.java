package com.khai.quizguru.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name="images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="title")
    private String title;

    @Column(name="path")
    private String path;
}

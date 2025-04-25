package com.example.letzteplf.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String review;
    private String rating;

    @JsonBackReference
    @ManyToOne
    private Post post;

    @ManyToOne
    private Student student;
}

package com.example.letzteplf.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @JsonBackReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Comment> comment;

}

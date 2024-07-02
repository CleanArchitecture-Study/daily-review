package org.example.day09_totalproject.learning.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdAt;
    private String updatedAt;
    private String name;
    private int price;
    private String description;
    private String image;
    private Boolean isDisplay;

    @OneToMany(mappedBy = "course")
    private List<Section> sections = new ArrayList<>();
}

package org.example.day09_totalproject.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdAt;
    private String updatedAt;
    private String impUid;

    @OneToOne
    private Course course;

    @OneToOne
    private Member member;
}

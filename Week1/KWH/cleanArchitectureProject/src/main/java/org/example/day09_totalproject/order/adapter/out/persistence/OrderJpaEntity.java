package org.example.day09_totalproject.order.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;

@Entity
@Table(name="Orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderJpaEntity {
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

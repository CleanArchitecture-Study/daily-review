package org.example.day09_totalproject.order.domain;

import lombok.Builder;
import lombok.Getter;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;

@Getter
@Builder
public class Order {
    private Long id;
    private String createdAt;
    private String updatedAt;
    private String impUid;

    private Course course;
    private Member member;

}

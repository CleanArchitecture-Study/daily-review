package org.example.day09_totalproject.order.adapter.out.persistence;

import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderJpaEntity, Integer> {
    Optional<OrderJpaEntity> findByCourseAndMember(Course course, Member member);
}

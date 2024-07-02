package org.example.day09_totalproject.orders;

import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findByCourseAndMember(Course course, Member member);
}

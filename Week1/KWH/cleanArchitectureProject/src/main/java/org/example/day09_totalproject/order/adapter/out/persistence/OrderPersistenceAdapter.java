package org.example.day09_totalproject.order.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.learning.course.CourseRepository;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.order.application.port.out.LoadCoursePort;
import org.example.day09_totalproject.order.application.port.out.RegisterOrder;
import org.example.day09_totalproject.order.domain.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements LoadCoursePort, RegisterOrder {
    private final OrderRepository orderRepository;
    private final CourseRepository courseRepository;
    private final OrderMapper orderMapper;

    @Override
    public Course loadCourse(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.orElse(null);
    }

    @Override
    public Order registerOrder(Order order) {
        OrderJpaEntity orderJpaEntity = orderMapper.toEntity(order);
        orderRepository.save(orderJpaEntity);
        return orderMapper.toDomain(orderJpaEntity);
    }
}

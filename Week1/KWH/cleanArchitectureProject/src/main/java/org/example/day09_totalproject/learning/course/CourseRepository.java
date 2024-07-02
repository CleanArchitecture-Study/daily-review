package org.example.day09_totalproject.learning.course;

import org.example.day09_totalproject.learning.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

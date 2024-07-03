package org.example.day09_totalproject.order.application.port.out;

import org.example.day09_totalproject.learning.course.model.Course;

public interface LoadCoursePort {
    Course loadCourse(Long id);
}

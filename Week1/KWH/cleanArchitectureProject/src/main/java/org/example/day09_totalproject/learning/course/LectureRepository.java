package org.example.day09_totalproject.learning.course;

import org.example.day09_totalproject.learning.course.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}

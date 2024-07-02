package org.example.day09_totalproject.learning.course.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCourseRes {
    private Long id;
    private String name;
    private String image;
    private String description;
    private int price;
}

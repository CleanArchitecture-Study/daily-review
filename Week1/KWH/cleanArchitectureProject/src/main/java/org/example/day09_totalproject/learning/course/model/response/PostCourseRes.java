package org.example.day09_totalproject.learning.course.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCourseRes {
    private Long id;
    private String name;
    private String description;
    private String image;
    private int price;

}

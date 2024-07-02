package org.example.day09_totalproject.learning.course.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetCourseDetailRes {
    private Long id;
    private String name;
    private String image;
    private Boolean ordered;
    private String description;
    private int price;
    private List<GetSectionRes> sections;

}

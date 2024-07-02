package org.example.day09_totalproject.learning.course.model.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class PostCourseReq {
    private String name;
    private String description;
    private int price;
    private List<PostSectionReq> sections;
}

package org.example.day09_totalproject.learning.course.model.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class PostSectionReq {
    private String name;
    private List<PostLectureReq> lectures;
}

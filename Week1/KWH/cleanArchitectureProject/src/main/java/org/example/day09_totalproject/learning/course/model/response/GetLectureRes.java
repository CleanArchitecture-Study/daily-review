package org.example.day09_totalproject.learning.course.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetLectureRes {
    private Long id;
    private String name;
    private int playTime;
    private String videoUrl;
}

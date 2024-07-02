package org.example.day09_totalproject.learning.course.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetSectionRes {
    private Long id;
    private String name;
    private List<GetLectureRes> lectures;
}

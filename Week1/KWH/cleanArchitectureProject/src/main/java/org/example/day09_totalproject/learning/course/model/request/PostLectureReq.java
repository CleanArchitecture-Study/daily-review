package org.example.day09_totalproject.learning.course.model.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class PostLectureReq {
    private String name;
    private int playTime;
    private String videoUrl;
}

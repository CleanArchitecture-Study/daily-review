package org.example.day09_totalproject.order.application.port.in;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetOrderRes {
    private String impUid;
    private Long courseId;
}

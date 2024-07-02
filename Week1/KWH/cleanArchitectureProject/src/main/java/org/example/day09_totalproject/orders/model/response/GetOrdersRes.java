package org.example.day09_totalproject.orders.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetOrdersRes {
    private String impUid;
    private Long courseId;
}

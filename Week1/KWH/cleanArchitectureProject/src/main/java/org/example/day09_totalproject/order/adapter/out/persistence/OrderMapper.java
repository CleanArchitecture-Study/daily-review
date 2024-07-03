package org.example.day09_totalproject.order.adapter.out.persistence;

import org.example.day09_totalproject.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderJpaEntity toEntity(Order order){
        if (order == null){
            return null;
        }
        return OrderJpaEntity.builder()
                .id(order.getId())
                .impUid(order.getImpUid())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .member(order.getMember())
                .course(order.getCourse())
                .build();
    }

    public Order toDomain(OrderJpaEntity entity){
        if (entity == null){
            return null;
        }
        return Order.builder()
                .id(entity.getId())
                .impUid(entity.getImpUid())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .member(entity.getMember())
                .course(entity.getCourse())
                .build();
    }
}

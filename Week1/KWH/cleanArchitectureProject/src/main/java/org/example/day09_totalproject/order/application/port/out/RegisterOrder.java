package org.example.day09_totalproject.order.application.port.out;

import org.example.day09_totalproject.order.domain.Order;

public interface RegisterOrder {
    Order registerOrder(Order order);
}

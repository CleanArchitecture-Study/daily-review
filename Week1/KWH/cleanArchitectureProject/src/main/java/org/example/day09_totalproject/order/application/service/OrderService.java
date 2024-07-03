package org.example.day09_totalproject.order.application.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.order.application.port.in.GetOrderCourseUseCase;
import org.example.day09_totalproject.order.application.port.in.GetOrderRes;
import org.example.day09_totalproject.order.application.port.in.OrderUseCase;
import org.example.day09_totalproject.order.application.port.out.LoadCoursePort;
import org.example.day09_totalproject.order.application.port.out.RegisterOrder;
import org.example.day09_totalproject.order.domain.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase, GetOrderCourseUseCase {

    private final IamportClient iamportClient;
    private final LoadCoursePort loadCoursePort;
    private final RegisterOrder registerOrder;

    @Override
    public IamportResponse<Payment> getOrderInfo(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    @Override
    public boolean checkValidateOrder(IamportResponse<Payment> iamportResponse, Course course){
        Payment payment = iamportResponse.getResponse();
        int amount = payment.getAmount().intValue();
        return amount == course.getPrice();
    }

    @Override
    public GetOrderRes registerOrder(Course course, Member member, String impUid) {
        String now = String.valueOf(LocalDateTime.now());
        Order order = Order.builder()
                .impUid(impUid)
                .course(course)
                .member(member)
                .createdAt(now)
                .updatedAt(now)
                .build();
        order = registerOrder.registerOrder(order);
        return GetOrderRes.builder()
                .courseId(course.getId())
                .impUid(impUid)
                .build();
    }

    @Override
    public Course getOrderCourse(IamportResponse<Payment> iamportResponse){
        Payment payment = iamportResponse.getResponse();
        Long courseId = Long.valueOf(payment.getMerchantUid().split("_")[1]);
        System.out.println(courseId);
        return loadCoursePort.loadCourse(courseId);
    }

}

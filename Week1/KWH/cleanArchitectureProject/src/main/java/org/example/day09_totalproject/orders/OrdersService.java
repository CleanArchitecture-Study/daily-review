package org.example.day09_totalproject.orders;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.learning.course.CourseRepository;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.orders.model.Orders;
import org.example.day09_totalproject.orders.model.response.GetOrdersRes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final IamportClient iamportClient;
    private final OrdersRepository ordersRepository;
    private final CourseRepository courseRepository;

    public IamportResponse<Payment> getOrderInfo(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    public Course getOrderCourse(IamportResponse<Payment> iamportResponse){
        Payment payment = iamportResponse.getResponse();
        Long courseId = Long.valueOf(payment.getMerchantUid().split("_")[1]);
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()){
            Course course = optionalCourse.get();
            return course;
        }
        return null;
    }

    public boolean checkValidateOrder(IamportResponse<Payment> iamportResponse, Course course){
        Payment payment = iamportResponse.getResponse();
        System.out.println(payment);
        int amount = payment.getAmount().intValue();
        return amount == course.getPrice();
    }

    public GetOrdersRes registerOrders(Course course, Member member, String impUid){
        String now = String.valueOf(LocalDateTime.now());
        Orders orders = Orders.builder()
                .impUid(impUid)
                .course(course)
                .member(member)
                .createdAt(now)
                .updatedAt(now)
                .build();
        ordersRepository.save(orders);
        return GetOrdersRes.builder()
                .courseId(course.getId())
                .impUid(impUid)
                .build();
    }
}

package org.example.day09_totalproject.order.application.port.in;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.example.day09_totalproject.learning.course.model.Course;

public interface GetOrderCourseUseCase {
    Course getOrderCourse(IamportResponse<Payment> iamportResponse);
}

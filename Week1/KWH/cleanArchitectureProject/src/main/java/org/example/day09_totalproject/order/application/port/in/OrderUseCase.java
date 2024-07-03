package org.example.day09_totalproject.order.application.port.in;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.Member;

import java.io.IOException;

public interface OrderUseCase {
    boolean checkValidateOrder(IamportResponse<Payment> iamportResponse, Course course);
    GetOrderRes registerOrder(Course course, Member member, String impUid);
    IamportResponse<Payment> getOrderInfo(String impUid) throws IamportResponseException, IOException;
}

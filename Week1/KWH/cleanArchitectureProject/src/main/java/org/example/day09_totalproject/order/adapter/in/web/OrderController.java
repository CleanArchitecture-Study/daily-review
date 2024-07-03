package org.example.day09_totalproject.order.adapter.in.web;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.common.BaseResponse;
import org.example.day09_totalproject.common.BaseResponseStatus;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.CustomUserDetails;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.order.application.port.in.GetOrderCourseUseCase;
import org.example.day09_totalproject.order.application.port.in.GetOrderRes;
import org.example.day09_totalproject.order.application.port.in.OrderUseCase;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderUseCase orderUseCase;
    private final GetOrderCourseUseCase getOrderCourseUseCase;

    @GetMapping("/validation")
    public BaseResponse<GetOrderRes> validation(@AuthenticationPrincipal CustomUserDetails customUserDetails, String impUid){
        Member member = null;
        if (customUserDetails != null){
            member = customUserDetails.getMember();
        }
        try{
            IamportResponse<Payment> iamportResponse = orderUseCase.getOrderInfo(impUid);
            Course orderCourse = getOrderCourseUseCase.getOrderCourse(iamportResponse);
            if (orderCourse == null){
                return new BaseResponse<>(BaseResponseStatus.ORDERS_VALIDATION_FAIL);
            }
            boolean result = orderUseCase.checkValidateOrder(iamportResponse, orderCourse);
            if(result){
                GetOrderRes getOrderRes = orderUseCase.registerOrder(orderCourse, member, impUid);
                return new BaseResponse<>(getOrderRes);
            } else{
                return new BaseResponse<>(BaseResponseStatus.IAMPORT_ERROR);
            }

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

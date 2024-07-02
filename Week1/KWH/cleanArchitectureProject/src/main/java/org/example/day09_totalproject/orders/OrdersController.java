package org.example.day09_totalproject.orders;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.common.BaseResponse;
import org.example.day09_totalproject.common.BaseResponseStatus;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.member.model.CustomUserDetails;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.orders.model.response.GetOrdersRes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @GetMapping("/validation")
    public BaseResponse<GetOrdersRes> validation(@AuthenticationPrincipal CustomUserDetails customUserDetails, String impUid){
        Member member = null;
        if (customUserDetails != null){
            member = customUserDetails.getMember();
        }
        try{
            IamportResponse<Payment> iamportResponse = ordersService.getOrderInfo(impUid);
            Course orderCourse = ordersService.getOrderCourse(iamportResponse);
            if (orderCourse == null){
                return new BaseResponse<>(BaseResponseStatus.ORDERS_VALIDATION_FAIL);
            }
            boolean result = ordersService.checkValidateOrder(iamportResponse, orderCourse);
            if(result){
                GetOrdersRes getOrdersRes = ordersService.registerOrders(orderCourse, member, impUid);
                return new BaseResponse<>(getOrdersRes);
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

package org.example.day09_totalproject.learning.course;

import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.common.BaseResponse;
import org.example.day09_totalproject.file.FileUploadService;
import org.example.day09_totalproject.learning.course.model.request.PostCourseReq;
import org.example.day09_totalproject.learning.course.model.response.GetCourseDetailRes;
import org.example.day09_totalproject.learning.course.model.response.GetCourseRes;
import org.example.day09_totalproject.learning.course.model.response.PostCourseRes;
import org.example.day09_totalproject.member.model.CustomUserDetails;
import org.example.day09_totalproject.member.model.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final FileUploadService fileUploadService;

    @PostMapping("/create")
    public BaseResponse<PostCourseRes> createCourse(@RequestPart MultipartFile image, @RequestPart PostCourseReq request) {
        String upload = fileUploadService.upload(image);
        PostCourseRes postCourseRes = courseService.saveCourse(upload, request);
        return new BaseResponse<>(postCourseRes);
    }

    @GetMapping("/{id}")
    public BaseResponse<GetCourseDetailRes> getCourseById(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id) {
        Member member = null;
        if (customUserDetails !=null){
            member = customUserDetails.getMember();
        }
        GetCourseDetailRes getCourseDetailRes = courseService.read(id, member);
        return new BaseResponse<>(getCourseDetailRes);
    }

    @GetMapping("/list")
    public BaseResponse<List<GetCourseRes>> getCourseList(){
        List<GetCourseRes> getCourseResList = courseService.list();
        return new BaseResponse<>(getCourseResList);
    }
}

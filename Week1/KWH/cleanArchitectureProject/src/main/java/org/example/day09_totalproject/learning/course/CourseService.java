package org.example.day09_totalproject.learning.course;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.day09_totalproject.learning.course.model.Course;
import org.example.day09_totalproject.learning.course.model.Lecture;
import org.example.day09_totalproject.learning.course.model.Section;
import org.example.day09_totalproject.learning.course.model.request.PostCourseReq;
import org.example.day09_totalproject.learning.course.model.request.PostLectureReq;
import org.example.day09_totalproject.learning.course.model.request.PostSectionReq;
import org.example.day09_totalproject.learning.course.model.response.*;
import org.example.day09_totalproject.member.model.Member;
import org.example.day09_totalproject.orders.OrdersRepository;
import org.example.day09_totalproject.orders.model.Orders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final LectureRepository lectureRepository;
    private final OrdersRepository ordersRepository;

    @Transactional
    public PostCourseRes saveCourse(String image, PostCourseReq postCourseReq) {
        String nowTime = String.valueOf(LocalDateTime.now());
        Course course = Course.builder()
                .name(postCourseReq.getName())
                .image(image)
                .price(postCourseReq.getPrice())
                .createdAt(nowTime)
                .updatedAt(nowTime)
                .description(postCourseReq.getDescription())
                .isDisplay(true)
                .build();
        courseRepository.save(course);

        List<PostSectionReq> sections = postCourseReq.getSections();
        for(PostSectionReq postSectionReq : sections) {
            Section section = Section.builder()
                    .name(postSectionReq.getName())
                    .createdAt(nowTime)
                    .updatedAt(nowTime)
                    .course(course)
                    .build();
            sectionRepository.save(section);
            List<PostLectureReq> lectures = postSectionReq.getLectures();
            for(PostLectureReq postLectureReq : lectures) {
                Lecture lecture = Lecture.builder()
                        .name(postLectureReq.getName())
                        .createdAt(nowTime)
                        .updatedAt(nowTime)
                        .playTime(postLectureReq.getPlayTime())
                        .videoUrl(postLectureReq.getVideoUrl())
                        .section(section)
                        .build();
                lectureRepository.save(lecture);
            }
        }
        return PostCourseRes.builder()
                .id(course.getId())
                .name(course.getName())
                .image(course.getImage())
                .price(course.getPrice())
                .description(course.getDescription())
                .build();
    }

    public GetCourseDetailRes read(Long id, Member member){
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if(optionalCourse.isPresent()){
            Course course = optionalCourse.get();
            List<Section> sections = course.getSections();
            List<GetSectionRes> getSectionResList = new ArrayList<>();
            for(Section section : sections){
                List<Lecture> lectures = section.getLectures();
                List<GetLectureRes> getLectureResList = new ArrayList<>();
                for(Lecture lecture : lectures){
                    GetLectureRes getLectureRes = GetLectureRes.builder()
                            .name(lecture.getName())
                            .id(lecture.getId())
                            .videoUrl(lecture.getVideoUrl())
                            .playTime(lecture.getPlayTime())
                            .build();
                    getLectureResList.add(getLectureRes);
                }
                GetSectionRes getSectionRes = GetSectionRes.builder()
                        .name(section.getName())
                        .id(section.getId())
                        .lectures(getLectureResList)
                        .build();
                getSectionResList.add(getSectionRes);
            }
            Optional<Orders> optionalOrders = ordersRepository.findByCourseAndMember(course, member);
            GetCourseDetailRes getCourseDetailRes = GetCourseDetailRes.builder()
                    .description(course.getDescription())
                    .image(course.getImage())
                    .price(course.getPrice())
                    .ordered(optionalOrders.isPresent())
                    .id(course.getId())
                    .name(course.getName())
                    .sections(getSectionResList)
                    .build();
            return getCourseDetailRes;
        }
        return null;
    }

    public List<GetCourseRes> list(){
        List<Course> courseList = courseRepository.findAll();
        List<GetCourseRes> getCourseResList = new ArrayList<>();
        for (Course course : courseList) {
            GetCourseRes getCourseRes = GetCourseRes.builder()
                    .price(course.getPrice())
                    .image(course.getImage())
                    .name(course.getName())
                    .id(course.getId())
                    .description(course.getDescription())
                    .build();
            getCourseResList.add(getCourseRes);
        }
        return getCourseResList;
    }
}

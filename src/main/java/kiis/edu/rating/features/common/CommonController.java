package kiis.edu.rating.features.common;

import kiis.edu.rating.features.subject.base.SubjectRepository;
import kiis.edu.rating.features.subject.comment.SubjectCommentRepository;
import kiis.edu.rating.features.subject.commentReact.SubjectCommentReactRepository;
import kiis.edu.rating.features.subject.rating.SubjectRatingRepository;
import kiis.edu.rating.features.teacher.base.TeacherRepository;
import kiis.edu.rating.features.teacher.comment.TeacherCommentRepository;
import kiis.edu.rating.features.teacher.commentReact.TeacherCommentReactRepository;
import kiis.edu.rating.features.teacher.rating.TeacherRatingRepository;
import kiis.edu.rating.features.user.UserRepository;
import kiis.edu.rating.features.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/common")
public class CommonController {
    //    private final CommonRepository commonRepository;
    private final UserRepository userRepository;

    private final SubjectRepository subjectRepository;
    private final SubjectRatingRepository subjectRatingRepository;
    private final SubjectCommentRepository subjectCommentRepository;
    private final SubjectCommentReactRepository subjectCommentReactRepository;

    private final TeacherRepository teacherRepository;
    private final TeacherRatingRepository teacherRatingRepository;
    private final TeacherCommentRepository teacherCommentRepository;
    private final TeacherCommentReactRepository teacherCommentReactRepository;

    @GetMapping("/statistics")
    public StatisticsEntity getStatistics() {

        final StatisticsEntity statistics = new StatisticsEntity();

        statistics.totalUser = userRepository.count();
        statistics.totalAdmin = userRepository.countByRole(UserRole.ADMIN);
        statistics.totalManager = userRepository.countByRole(UserRole.MANAGER);

        statistics.totalSubject = subjectRepository.count();
        statistics.totalSubjectRating = subjectRatingRepository.count();
        statistics.totalSubjectComment = subjectCommentRepository.count();
        statistics.totalSubjectCommentReact = subjectCommentReactRepository.count();

        statistics.totalTeacher = teacherRepository.count();
        statistics.totalTeacherRating = teacherRatingRepository.count();
        statistics.totalTeacherComment = teacherCommentRepository.count();
        statistics.totalTeacherCommentReact = teacherCommentReactRepository.count();

        return statistics;
    }
}

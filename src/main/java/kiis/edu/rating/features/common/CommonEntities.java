package kiis.edu.rating.features.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
class StatisticsEntity extends BaseEntity{
    public long totalUser, totalManager, totalAdmin,
            totalSubject, totalSubjectComment, totalSubjectCommentReact, totalSubjectRating,
            totalTeacher, totalTeacherComment, totalTeacherCommentReact, totalTeacherRating;
}

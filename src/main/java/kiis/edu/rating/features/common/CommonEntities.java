package kiis.edu.rating.features.common;

import kiis.edu.rating.features.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
class StatisticsEntity extends BaseEntity{
    public int totalUser, totalManager, totalAdmin,
            totalSubject, totalSubjectComment, totalSubjectCommentReact, totalSubjectRating,
            totalTeacher, totalTeacherComment, totalTeacherCommentReact, totalTeacherRating;
}

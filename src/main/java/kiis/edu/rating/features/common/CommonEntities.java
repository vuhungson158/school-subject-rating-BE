package kiis.edu.rating.features.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "statistic_view")
@AllArgsConstructor
@NoArgsConstructor
class StatisticsEntity {
    @Id
    private Long id;
    public long totalUser, totalManager, totalAdmin,
            totalSubject, totalSubjectComment, totalSubjectCommentReact, totalSubjectRating,
            totalTeacher, totalTeacherComment, totalTeacherCommentReact, totalTeacherRating;
}

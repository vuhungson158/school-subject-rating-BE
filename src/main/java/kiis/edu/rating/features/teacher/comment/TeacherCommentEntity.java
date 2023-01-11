package kiis.edu.rating.features.teacher.comment;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name = "teacher_comment")
@AllArgsConstructor
class TeacherCommentEntity extends BaseTeacherCommentEntity { }

@SuppressWarnings("unused")
@Entity
class TeacherCommentWithLikeCount extends BaseTeacherCommentEntity {
    public int likeCount;
    public int dislikeCount;
}

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseTeacherCommentEntity extends BaseEntity {
    public long userId, teacherId;
    public String comment;
}

package kiis.edu.rating.features.teacher.commentReact;

import kiis.edu.rating.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teacher_comment_react")
@AllArgsConstructor
@NoArgsConstructor
class TeacherCommentReactEntity extends BaseEntity {
    public long userId, commentId;
    public boolean react;
}

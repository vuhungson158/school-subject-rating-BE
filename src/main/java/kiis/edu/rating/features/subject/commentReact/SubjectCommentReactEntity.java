package kiis.edu.rating.features.subject.commentReact;

import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "subject_comment_react")
@AllArgsConstructor
@NoArgsConstructor
class SubjectCommentReactEntity extends BaseEntity {
    public long userId, commentId;
    public boolean react;
}

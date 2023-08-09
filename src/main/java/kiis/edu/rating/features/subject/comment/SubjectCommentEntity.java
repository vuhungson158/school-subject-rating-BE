package kiis.edu.rating.features.subject.comment;

import kiis.edu.rating.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name = "subject_comment")
@AllArgsConstructor
class SubjectCommentEntity extends BaseSubjectCommentEntity {
}

@Entity
class SubjectCommentWithLikeCount extends BaseSubjectCommentEntity {
    public int likeCount;
    public int dislikeCount;
    public String displayName;
}

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
abstract class BaseSubjectCommentEntity extends BaseEntity {
    public long userId, subjectId;
    public String comment;
}

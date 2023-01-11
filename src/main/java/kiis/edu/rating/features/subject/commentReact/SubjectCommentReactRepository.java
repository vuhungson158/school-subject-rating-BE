package kiis.edu.rating.features.subject.commentReact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface SubjectCommentReactRepository extends JpaRepository<SubjectCommentReactEntity, Long> {
    List<SubjectCommentReactEntity> findByCommentId(long commentId);

    boolean existsByUserIdAndCommentId(long userId, long commentId);
}

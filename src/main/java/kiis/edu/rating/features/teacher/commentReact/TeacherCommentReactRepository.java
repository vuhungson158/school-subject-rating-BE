package kiis.edu.rating.features.teacher.commentReact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface TeacherCommentReactRepository extends JpaRepository<TeacherCommentReactEntity, Long> {
    List<TeacherCommentReactEntity> findByCommentId(long commentId);

    boolean existsByUserIdAndCommentId(long userId, long commentId);
}

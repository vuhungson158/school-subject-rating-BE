package kiis.edu.rating.features.subject.commentReact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectCommentReactRepository extends JpaRepository<SubjectCommentReactEntity, Long> {
    List<SubjectCommentReactEntity> findByCommentId(long commentId);

    boolean existsByUserIdAndCommentId(long userId, long commentId);

    @Query(nativeQuery = true, value =
            "select *"
                    + " \n from subject_comment_react"
                    + " \n where comment_id  in (:list)"
                    + " \n and user_id = :userId"
    )
    List<SubjectCommentReactEntity> findByUserIdAndCommentIdList(
            @Param("userId") long userId, @Param("list") List<Long> list);
}

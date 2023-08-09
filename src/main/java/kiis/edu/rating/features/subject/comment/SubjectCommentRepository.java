package kiis.edu.rating.features.subject.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectCommentRepository extends JpaRepository<SubjectCommentEntity, Long> {
    List<SubjectCommentEntity> findAllByIsDeleted(boolean disable);

    boolean existsBySubjectIdAndUserId(long subjectId, long userId);
}

@Repository
interface SubjectCommentWithLikeCountRepository extends JpaRepository<SubjectCommentWithLikeCount, Long> {
    @Query(nativeQuery = true, value =
            "select"
                    + " \n subject_comment.*, "
                    + " \n (select display_name from auth_user where id = subject_comment.user_id),"
                    + " \n count(react = true or null) as like_count,"
                    + " \n count(react = false or null) as dislike_count"
                    + " \n from subject_comment"
                    + " \n left join subject_comment_react"
                    + " \n on subject_comment.id = subject_comment_react.comment_id"
                    + " \n group by subject_comment.id"
                    + " \n having subject_comment.subject_id = :subjectId"
                    + " \n order by count(subject_comment_react) desc  "
                    + " \n limit :limit offset :page "
    )
    List<SubjectCommentWithLikeCount> findTopBySubjectId(
            @Param("limit") int limit, @Param("page") int page, @Param("subjectId") long subjectId
    );

    @Query(nativeQuery = true, value =
            "select"
                    + " \n count(*) "
                    + " \n from subject_comment"
                    + " \n where subject_comment.subject_id = :subjectId"
    )
    int countCommentBySubjectId(@Param("subjectId") long subjectId);

    @Query(nativeQuery = true, value =
            "select"
                    + " \n subject_comment.*, "
                    + " \n (select display_name from auth_user where id = :userId),"
                    + " \n count(react = true or null) as like_count,"
                    + " \n count(react = false or null) as dislike_count"
                    + " \n from subject_comment"
                    + " \n left join subject_comment_react"
                    + " \n on subject_comment.id = subject_comment_react.comment_id"
                    + " \n group by subject_comment.id"
                    + " \n having subject_comment.user_id = :userId"
                    + " \n and subject_comment.subject_id = :subjectId"
                    + " \n limit 1"
    )
    SubjectCommentWithLikeCount findBySubjectIdAndUserId(@Param("subjectId") long subjectId, @Param("userId") long userId);

    @Query(nativeQuery = true, value =
            "select"
                    + " \n subject_comment.*, "
                    + " \n (select display_name from auth_user where id = subject_comment.user_id limit 1),"
                    + " \n count(react = true or null) as like_count,"
                    + " \n count(react = false or null) as dislike_count"
                    + " \n from subject_comment"
                    + " \n left join subject_comment_react"
                    + " \n on subject_comment.id = subject_comment_react.comment_id"
                    + " \n group by subject_comment.id"
                    + " \n having subject_comment.id = :id"
                    + " \n limit 1"
    )
    Optional<SubjectCommentWithLikeCount> findById(@Param("id") long id);
}

package kiis.edu.rating.features.teacher.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherCommentRepository extends JpaRepository<TeacherCommentEntity, Long> {
    List<TeacherCommentEntity> findAllByDisable(boolean disable);

    boolean existsByTeacherIdAndUserId(long teacherId, long userId);
}

@Repository
interface TeacherCommentWithLikeCountRepository extends JpaRepository<TeacherCommentWithLikeCount, Long> {
    //    TODO: Rewrite Query
    @Query(nativeQuery = true, value =
            "select comment.*, count(if(react=1,1,null)) as like_count, count(if(react=0,1,null)) as dislike_count from "
                    + "( "
                    + "select * from comment "
                    + "where and ref_id = :teacherId "
                    + ") "
                    + "as comment "
                    + "left join comment_rating "
                    + "on comment.id = comment_rating.comment_id "
                    + "group by comment.id "
                    + "order by count(*) desc limit :page, :limit "
    )
    List<TeacherCommentWithLikeCount> findTopRatingComment(
            @Param("limit") int limit, @Param("page") int page, @Param("teacherId") long teacherId
    );
}

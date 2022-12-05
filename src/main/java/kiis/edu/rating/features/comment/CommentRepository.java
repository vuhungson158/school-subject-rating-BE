package kiis.edu.rating.features.comment;

import kiis.edu.rating.features.subject.rating.SubjectRatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByDisable(boolean disable);

    Optional<CommentEntity> findByRefTableAndRefIdAndUserId(String refTable, long refId, long userId);

    @Query(nativeQuery = true, value =
            "select * from "
                    + "( "
                    + "select * from comment "
                    + "where ref_table = :refTable and ref_id = :refId "
                    + ") "
                    + "as comment "
                    + "left join comment_rating "
                    + "on comment.id = comment_rating.comment_id "
                    + "group by comment.id "
                    + "order by count(*) desc limit :page, :limit "
    )
    List<CommentEntity> findTopRatingComment(
            @Param("limit") int limit, @Param("page") int page,
            @Param("refTable") String refTable, @Param("refId") long refId
    );
}

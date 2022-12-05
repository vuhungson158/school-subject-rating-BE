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

//    @Query(nativeQuery = true, value =
//            "SELECT * FROM ("
//                    + "SELECT p.*, COUNT(IF(v.positive=1,1,NULL)) AS vote_up, COUNT(IF(v.positive=0,1,NULL)) AS vote_down FROM ("
//                    + "SELECT * FROM place AS p WHERE"
//                    + " p.latitude > :#{#f.latMin} AND p.latitude < :#{#f.latMax}"
//                    + " AND p.longitude > :#{#f.lngMin} AND p.longitude < :#{#f.lngMax}"
//                    + " AND (:#{#f.privateWc} = FALSE OR p.private_wc = 1)"
//                    + " AND (:#{#f.airConditioner} = FALSE OR p.air_conditioner = 1)"
//                    + " AND (:#{#f.hotWater} = FALSE OR p.hot_water = 1)"
//                    + " AND (:#{#f.fridge} = FALSE OR p.fridge = 1)"
//                    + " AND (:#{#f.washer} = FALSE OR p.washer = 1)"
//                    + " AND (:#{#f.nonCurfew} = FALSE OR p.curfew IS NULL)"
//                    + " AND p.acreage_min >= :#{#f.acreageMin}"
//                    + " AND (:#{#f.priceMax} IS NULL OR p.price_max <= :#{#f.priceMax})"
//                    + ") AS p LEFT JOIN vote AS v ON p.id=v.place_id"
//                    + " GROUP BY p.id) AS p2"
//                    + " ORDER BY vote_up - vote_down DESC LIMIT 5")
//    List<CommentEntity> findTopRatingComment(
//            @Param("limit") int limit, @Param("page") int page,
//            @Param("refTable") String refTable, @Param("refId") long refId
//    );

}

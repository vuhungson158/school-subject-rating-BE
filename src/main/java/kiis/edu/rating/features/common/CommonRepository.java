package kiis.edu.rating.features.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonRepository extends JpaRepository<StatisticsEntity, Long> {
    @Query(nativeQuery = true, value =
            "select * from statistic_view"
    )
    StatisticsEntity findStatistics();
}

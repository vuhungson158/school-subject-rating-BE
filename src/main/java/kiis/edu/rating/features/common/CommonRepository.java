package kiis.edu.rating.features.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonRepository extends JpaRepository<StatisticsEntity, Long> {
    @Query(nativeQuery = true, value =
            "select "
                    + " \n *,"
                    + " \n (select count(role = 'USER' or null) from auth_user limit 1) as total_user,"
                    + " \n (select count(role = 'MANAGER' or null) from auth_user limit 1) as total_manager,"
                    + " \n (select count(role = 'ADMIN' or null) from auth_user limit 1) as total_admin,"
                    + " \n (select count(disable = false or null) from subject limit 1) as total_subject,"
                    + " \n (select count(disable = false or null) from subject_comment limit 1) as total_subject_comment,"
                    + " \n (select count(disable = false or null) from subject_comment_react scr  limit 1) as total_subject_comment_react,"
                    + " \n (select count(disable = false or null) from subject_rating sr  limit 1) as total_subject_rating,"
                    + " \n (select count(disable = false or null) from teacher limit 1) as total_teacher,"
                    + " \n (select count(disable = false or null) from teacher_comment limit 1) as total_teacher_comment,"
                    + " \n (select count(disable = false or null) from teacher_comment_react limit 1) as total_teacher_comment_react,"
                    + " \n (select count(disable = false or null) from teacher_rating limit 1) as total_teacher_rating"
                    + " \n from auth_user where true limit 1"
    )
    StatisticsEntity findStatistics();
}

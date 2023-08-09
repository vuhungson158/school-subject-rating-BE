package kiis.edu.rating;

import kiis.edu.rating.common.BaseEntity;
import kiis.edu.rating.features.subject.base.SubjectEntity;
import kiis.edu.rating.features.teacher.base.TeacherEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class RatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatingApplication.class, args);
    }

}

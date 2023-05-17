package kiis.edu.rating.features.subject.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kiis.edu.rating.features.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subject_plan")
@AllArgsConstructor
@NoArgsConstructor
public class SubjectPlan extends BaseEntity {

    public long userId;

    @JsonIgnore
    public String subjectIds;

    @Transient
    public Set<Integer> getSubjectIdList() {
        return Arrays.stream(subjectIds.split("_"))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toSet());
    }
}

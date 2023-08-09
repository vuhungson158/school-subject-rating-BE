package kiis.edu.rating.features.subject.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kiis.edu.rating.common.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "subject_plan")
//@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class SubjectPlanEntity extends BaseEntity {

    public final long userId;

    private String subjectIds;

    @ElementCollection
    @Transient
    @SuppressWarnings("unused")
    public List<Long> getSubjectIdList() {
        return Arrays.stream(subjectIds.split("_"))
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
    }

    @JsonIgnore
    public void setSubjectIds(List<Long> subjectIdList) {
        this.subjectIds = subjectIdList.stream()
                .map(Object::toString)
                .collect(Collectors.joining("_"));
    }
}

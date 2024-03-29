package kiis.edu.rating.features.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @CreatedDate
    @Column(insertable = false, updatable = false)
    public Instant createdAt;
    @LastModifiedDate
    @Column(insertable = false, updatable = false)
    public Instant updatedAt;
    public boolean disable = false;
}

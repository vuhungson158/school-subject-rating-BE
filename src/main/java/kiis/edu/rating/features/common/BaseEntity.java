package kiis.edu.rating.features.common;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public Instant createdAt, updatedAt;

}

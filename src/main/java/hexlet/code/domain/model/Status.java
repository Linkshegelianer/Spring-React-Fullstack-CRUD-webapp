package hexlet.code.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity @Table(name = "statuses")
public class Status {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @NotBlank
    private String name;

    @CreationTimestamp
    private Instant createdAt;
}

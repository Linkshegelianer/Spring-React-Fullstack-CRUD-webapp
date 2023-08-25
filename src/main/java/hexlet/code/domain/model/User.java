package hexlet.code.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity @Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @NotBlank
    private String firstName;

    @NonNull
    @NotBlank
    private String lastName;

    @NonNull
    @NotBlank
    @Email
    private String email;

    @NonNull
    @NotBlank
    @Size(min = 3)
    private String password;

    @CreationTimestamp
    private Instant createdAt;
}

package project.code.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@Entity @Table(name = "labels")
public class Label {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @NotBlank
    private String name;

    @CreationTimestamp
    private Instant createdAt;
}

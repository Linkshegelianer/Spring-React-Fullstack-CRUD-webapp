package hexlet.code.domain.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelResponseDTO {

    private long id;
    private String name;
    private Instant createdAt;
}

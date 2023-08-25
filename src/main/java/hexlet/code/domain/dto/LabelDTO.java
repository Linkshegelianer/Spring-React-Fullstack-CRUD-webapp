package hexlet.code.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class LabelDTO {

    private long id;

    @NonNull
    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    private Instant createdAt;
}

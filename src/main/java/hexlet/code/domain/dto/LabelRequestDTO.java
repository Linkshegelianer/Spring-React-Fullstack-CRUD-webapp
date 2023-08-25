package hexlet.code.domain.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LabelRequestDTO {

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;
}

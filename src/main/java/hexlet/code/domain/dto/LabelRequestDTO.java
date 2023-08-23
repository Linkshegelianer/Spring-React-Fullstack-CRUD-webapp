package hexlet.code.domain.dto;

import javax.validation.constraints.NotBlank;

public class LabelRequestDTO {

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    public LabelRequestDTO() {
    }

    public LabelRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package hexlet.code.domain.dto;

import javax.validation.constraints.NotBlank;

public class StatusRequestDTO {

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    public StatusRequestDTO() {
    }

    public StatusRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

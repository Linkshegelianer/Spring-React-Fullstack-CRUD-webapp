package hexlet.code.domain.dto;

import javax.validation.constraints.NotBlank;

public class TaskStatusRequestDTO {

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    public TaskStatusRequestDTO() {
    }

    public TaskStatusRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

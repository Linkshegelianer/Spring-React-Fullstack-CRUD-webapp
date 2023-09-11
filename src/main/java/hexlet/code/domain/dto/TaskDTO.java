package hexlet.code.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private long id;
    private UserResponseDTO author;
    private UserResponseDTO executor;
    private StatusDTO taskStatus;
    private List<LabelDTO> labels;

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    private String description;

    private Instant createdAt;

    private Long executorId;

    @NotNull(message = "Field 'taskStatusId' must not be null!")
    private Long taskStatusId;

    private List<Long> labelIds;

    public TaskDTO(String name, String description, long executorId, long taskStatusId) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskStatusId = taskStatusId;
    }
}

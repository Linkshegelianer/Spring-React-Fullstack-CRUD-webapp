package hexlet.code.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TaskRequestDTO {

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    private String description;

    private Long executorId;

    @NotNull(message = "Field 'taskStatusId' must not be null!")
    private Long taskStatusId;

    private List<Long> labelIds;

    public TaskRequestDTO() {
    }

    public TaskRequestDTO(String name,
                          String description,
                          Long executorId,
                          Long taskStatusId,
                          List<Long> labelIds) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskStatusId = taskStatusId;
        this.labelIds = labelIds;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public Long getTaskStatusId() {
        return taskStatusId;
    }

    public List<Long> getLabelIds() {
        return labelIds;
    }
}

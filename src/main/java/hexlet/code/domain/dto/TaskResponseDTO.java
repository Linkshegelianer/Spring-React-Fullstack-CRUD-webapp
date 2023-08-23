package hexlet.code.domain.dto;

import java.time.Instant;
import java.util.List;

public class TaskResponseDTO {

    private long id;
    private UserResponseDTO author;
    private UserResponseDTO executor;
    private TaskStatusResponseDTO taskStatus;
    private List<LabelResponseDTO> labels;
    private String name;
    private String description;
    private Instant createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserResponseDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseDTO author) {
        this.author = author;
    }

    public UserResponseDTO getExecutor() {
        return executor;
    }

    public void setExecutor(UserResponseDTO executor) {
        this.executor = executor;
    }

    public TaskStatusResponseDTO getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusResponseDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<LabelResponseDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelResponseDTO> labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

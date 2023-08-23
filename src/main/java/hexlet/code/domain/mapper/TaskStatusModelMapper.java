package hexlet.code.domain.mapper;

import hexlet.code.domain.dto.TaskStatusRequestDTO;
import hexlet.code.domain.dto.TaskStatusResponseDTO;
import hexlet.code.domain.model.Status;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusModelMapper {

    public TaskStatusResponseDTO toTaskStatusResponseDTO(final Status status) {
        return new TaskStatusResponseDTO(
            status.getId(),
            status.getName(),
            status.getCreatedAt()
        );
    }

    public Status toTaskStatusModel(final TaskStatusRequestDTO dto) {
        return new Status(
            dto.getName()
        );
    }
}

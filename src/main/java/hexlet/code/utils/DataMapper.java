package hexlet.code.utils;

import hexlet.code.domain.dto.LabelDTO;
import hexlet.code.domain.dto.StatusDTO;
import hexlet.code.domain.dto.TaskDTO;
import hexlet.code.domain.dto.UserResponseDTO;
import hexlet.code.domain.model.Label;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.Task;
import hexlet.code.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataMapper {

    public UserResponseDTO toUserResponseDTO(final User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );
    }

    public LabelDTO toLabelDTO(final Label label) {
        return new LabelDTO(
                label.getId(),
                label.getName(),
                label.getCreatedAt()
        );
    }

    public StatusDTO toStatusDTO(final Status status) {
        return new StatusDTO(
                status.getId(),
                status.getName(),
                status.getCreatedAt()
        );
    }

    public TaskDTO toTaskDTO(final Task task) {
        final TaskDTO dto = new TaskDTO();
        if (task.getExecutor() != null) {
            dto.setExecutor(this.toUserResponseDTO(task.getExecutor()));
        }
        if (task.getLabels() != null) {
            List<LabelDTO> labelsDTO = task.getLabels()
                    .stream()
                    .map(this::toLabelDTO)
                    .sorted(Comparator.comparing(LabelDTO::getId))
                    .collect(Collectors.toList());
            dto.setLabels(labelsDTO);
        }
        if (task.getDescription() != null) {
            dto.setDescription(task.getDescription());
        }
        dto.setId(task.getId());
        dto.setAuthor(this.toUserResponseDTO(task.getAuthor()));
        dto.setTaskStatus(this.toStatusDTO(task.getTaskStatus()));
        dto.setName(task.getName());
        dto.setCreatedAt(task.getCreatedAt());
        return dto;
    }
}

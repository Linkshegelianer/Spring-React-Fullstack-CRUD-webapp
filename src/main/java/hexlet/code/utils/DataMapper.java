package hexlet.code.utils;

import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.StatusDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.UserResponseDTO;
import hexlet.code.domain.Label;
import hexlet.code.domain.Status;
import hexlet.code.domain.Task;
import hexlet.code.domain.User;
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

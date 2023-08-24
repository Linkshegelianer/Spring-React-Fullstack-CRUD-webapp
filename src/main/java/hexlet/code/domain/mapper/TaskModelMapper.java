package hexlet.code.domain.mapper;

import hexlet.code.domain.dto.LabelResponseDTO;
import hexlet.code.domain.dto.TaskResponseDTO;
import hexlet.code.domain.model.Task;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskModelMapper {

    private final StatusModelMapper taskStatusMapper;
    private final LabelModelMapper labelMapper;
    private final UserModelMapper userMapper;


    public TaskModelMapper(StatusModelMapper taskStatusMapper,
                           LabelModelMapper labelMapper,
                           UserModelMapper userMapper) {
        this.taskStatusMapper = taskStatusMapper;
        this.labelMapper = labelMapper;
        this.userMapper = userMapper;

    }

    public TaskResponseDTO toTaskResponseDTO(final Task task) {
        final TaskResponseDTO dto = new TaskResponseDTO();
        if (task.getExecutor() != null) {
            dto.setExecutor(userMapper.toUserResponseDTO(task.getExecutor()));
        }
        if (task.getLabels() != null) {
            List<LabelResponseDTO> labelsDTO = task.getLabels()
                .stream()
                .map(labelMapper::toLabelResponseDTO)
                .sorted(Comparator.comparing(LabelResponseDTO::getId))
                .collect(Collectors.toList());
            dto.setLabels(labelsDTO);
        }
        if (task.getDescription() != null) {
            dto.setDescription(task.getDescription());
        }
        dto.setId(task.getId());
        dto.setAuthor(userMapper.toUserResponseDTO(task.getAuthor()));
        dto.setTaskStatus(taskStatusMapper.toTaskStatusResponseDTO(task.getTaskStatus()));
        dto.setName(task.getName());
        dto.setCreatedAt(task.getCreatedAt());
        return dto;
    }
}

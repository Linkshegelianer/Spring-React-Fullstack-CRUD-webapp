package hexlet.code.service.impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.utils.TasksFactory;
import hexlet.code.dto.TaskDTO;
import hexlet.code.domain.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TasksFactory tasksFactory;

    @Transactional
    public Task createTask(TaskDTO dto, UserDetails authDetails) {
        Task newTask = tasksFactory.builder(new Task())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setTaskStatus(dto.getTaskStatusId())
                .setLabels(dto.getLabelIds())
                .setAuthor(authDetails.getUsername())
                .setExecutor(dto.getExecutorId())
                .build();

        return taskRepository.save(newTask);
    }

    public List<Task> getTasksByParams(Predicate predicate) {
        if (predicate == null) {
            return taskRepository.findAllByOrderByIdAsc();
        }
        return taskRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "id"));
    }

    public Task getTaskById(long id) {
        return taskRepository.findTaskById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id='%d' not found!".formatted(id)));
    }

    @Transactional
    public Task updateTask(long id, TaskDTO dto, UserDetails authDetails) {
        Task existedTask = getTaskById(id);

        return tasksFactory.builder(existedTask)
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setTaskStatus(dto.getTaskStatusId())
                .setLabels(dto.getLabelIds())
                .setExecutor(dto.getExecutorId())
                .build();
    }

    @Transactional
    public void deleteTask(long id, UserDetails authDetails) {
        Task existedTask = getTaskById(id);
        taskRepository.delete(existedTask);
    }
}

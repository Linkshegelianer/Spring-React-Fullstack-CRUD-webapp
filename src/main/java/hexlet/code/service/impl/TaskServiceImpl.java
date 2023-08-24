package hexlet.code.service.impl;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.builder.TasksFactory;
import hexlet.code.domain.dto.TaskRequestDTO;
import hexlet.code.domain.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TasksFactory tasksFactory;

    public TaskServiceImpl(TaskRepository taskRepository, TasksFactory tasksFactory) {
        this.taskRepository = taskRepository;
        this.tasksFactory = tasksFactory;
    }

    public List<Task> findTasksByParams(Predicate predicate) {
        if (predicate == null) {
            return taskRepository.findAllByOrderByIdAsc();
        }
        return taskRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "id"));
    }

    public Task findTaskById(long id) {
        return taskRepository.findTaskById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id='%d' not found!".formatted(id)));
    }

    @Transactional
    public Task createTask(TaskRequestDTO dto, UserDetails authDetails) {
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

    @Transactional
    public Task updateTask(long id, TaskRequestDTO dto, UserDetails authDetails) {
        Task existedTask = findTaskById(id);
        validateOwnerByEmail(existedTask.getAuthor().getEmail(), authDetails);

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
        Task existedTask = findTaskById(id);
        validateOwnerByEmail(existedTask.getAuthor().getEmail(), authDetails);
        taskRepository.delete(existedTask);
    }

    private void validateOwnerByEmail(String userEmail, UserDetails authDetails) {
        String authenticatedEmail = authDetails.getUsername();
        if (!authenticatedEmail.equalsIgnoreCase(userEmail)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}

package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createNewTask(final TaskDto taskDto) {
        final Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getTaskStatusId());
        task.setLabels(taskDto.getLabelList());
        task.setExecutor(taskDto.getExecutorId());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(final long id, final TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findTaskById(id).get();
        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setStatus(taskDto.getTaskStatusId());
        taskToUpdate.setLabels(taskDto.getLabelList());
        taskToUpdate.setExecutor(taskDto.getExecutorId());
        return taskRepository.save(taskToUpdate);
    }

    @Override
    public Task findTaskById(long id) throws ObjectNotFoundException {
        if (id != null) {
            return taskRepository.findTaskById(id)
                    .orElseThrow(() -> new ObjectNotFoundException());
        }
        return null;
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Task> findTasksByParams(Predicate predicate) {
        if (predicate == null) {
            return taskRepository.findAllByOrderByIdAsc();
        }
        return taskRepository.findAll(predicate, Sort.by(Sort.Direction.ASC, "id"));
    }
}

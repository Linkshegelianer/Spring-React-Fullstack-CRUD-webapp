package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

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
}

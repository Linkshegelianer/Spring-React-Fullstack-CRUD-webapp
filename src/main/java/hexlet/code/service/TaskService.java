package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.dto.TaskDTO;
import hexlet.code.domain.model.Task;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface TaskService {

    @Transactional
    public Task createTask(TaskDTO dto, UserDetails authDetails);

    public List<Task> getTasksByParams(Predicate predicate);

    public Task getTaskById(long id);

    @Transactional
    public Task updateTask(long id, TaskDTO dto, UserDetails authDetails);

    @Transactional
    public void deleteTask(long id, UserDetails authDetails);
}

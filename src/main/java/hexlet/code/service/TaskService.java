package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDTO;
import hexlet.code.domain.Task;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface TaskService {

    @Transactional
    Task createTask(TaskDTO dto, UserDetails authDetails);

    List<Task> getTasksByParams(Predicate predicate);

    Task getTaskById(long id);

    @Transactional
    Task updateTask(long id, TaskDTO dto, UserDetails authDetails);

    @Transactional
    void deleteTask(long id, UserDetails authDetails);
}

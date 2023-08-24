package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.dto.TaskRequestDTO;
import hexlet.code.domain.model.Task;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface TaskService {


    public List<Task> findTasksByParams(Predicate predicate);

    public Task findTaskById(long id);

    @Transactional
    public Task createTask(TaskRequestDTO dto, UserDetails authDetails);

    @Transactional
    public Task updateTask(long id, TaskRequestDTO dto, UserDetails authDetails);

    @Transactional
    public void deleteTask(long id, UserDetails authDetails);
}

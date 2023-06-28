package hexlet.code.controllers;

import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static hexlet.code.controllers.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    public static final String TASK_CONTROLLER_PATH = "/api/tasks";

    public static final String ID = "/{id}";

    @GetMapping(ID)
    public Task getTaskById(@PathVariable final long id) {
        return taskRepository.findById(id).get();
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .toList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Task addTask(@RequestBody @Valid final TaskDto dto) {
        return taskService.createNewTask(dto);
    }

    @PutMapping(ID)
    public Task updateTask(@RequestBody @Valid TaskDto dto, @PathVariable final long id) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping(ID)
    public void deleteTask(@PathVariable final long id) {
        taskRepository.deleteById(id);
    }
}

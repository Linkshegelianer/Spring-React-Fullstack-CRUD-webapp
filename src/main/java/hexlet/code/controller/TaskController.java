package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.dto.TaskRequestDTO;
import hexlet.code.domain.dto.TaskResponseDTO;
import hexlet.code.domain.mapper.TaskModelMapper;
import hexlet.code.domain.model.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${base.url}" + "/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskModelMapper taskMapper;

    public TaskController(TaskService taskService, TaskModelMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @Operation(summary = "Get task by parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is found",
                    content = {@Content(mediaType = "application/jsom", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping
    public List<TaskResponseDTO> findTasksByParams(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        List<Task> existedTasks = taskService.findTasksByParams(predicate);
        return existedTasks.stream()
            .map(taskMapper::toTaskResponseDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is found",
                    content = {@Content(mediaType = "application/jsom", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping(path = "/{id}")
    public TaskResponseDTO findTaskById(@PathVariable(name = "id") long id) {
        Task existedTask = taskService.findTaskById(id);
        return taskMapper.toTaskResponseDTO(existedTask);
    }

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task has been created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(@RequestBody @Valid TaskRequestDTO dto,
                                      @AuthenticationPrincipal UserDetails authDetails) {
        Task createdTask = taskService.createTask(dto, authDetails);
        return taskMapper.toTaskResponseDTO(createdTask);
    }

    @Operation(summary = "Update the task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is successfully updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    public TaskResponseDTO updateTask(@RequestBody @Valid TaskRequestDTO dto,
                                      @PathVariable(name = "id") long id,
                                      @AuthenticationPrincipal UserDetails authDetails) {
        Task updatedTask = taskService.updateTask(id, dto, authDetails);
        return taskMapper.toTaskResponseDTO(updatedTask);
    }

    @Operation(summary = "Delete the task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task has been successfully deleted"),
            @ApiResponse(responseCode = "404", description = "The task is not found", content = @Content)})
    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable(name = "id") long id,
                           @AuthenticationPrincipal UserDetails authDetails) {
        taskService.deleteTask(id, authDetails);
    }
}

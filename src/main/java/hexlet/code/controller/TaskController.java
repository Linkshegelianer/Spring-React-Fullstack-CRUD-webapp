package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.utils.DataMapper;
import hexlet.code.dto.TaskDTO;
import hexlet.code.domain.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base.url}" + "/tasks")
public class TaskController {

    private final TaskService taskService;

    private final DataMapper dataMapper;

    private static final String ONLY_AUTHOR_BY_ID =
            "@taskRepository.findTaskById(#id).get().getAuthor().getEmail().equals(authentication.getName())";

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task has been created",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@RequestBody @Valid TaskDTO dto,
                                      @AuthenticationPrincipal UserDetails authDetails) {
        Task createdTask = taskService.createTask(dto, authDetails);
        return dataMapper.toTaskDTO(createdTask);
    }

    @Operation(summary = "Get task by parameters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is found",
                content = {@Content(mediaType = "application/jsom",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping
    public List<TaskDTO> findTasksByParams(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        List<Task> existedTasks = taskService.getTasksByParams(predicate);
        return existedTasks.stream()
            .map(dataMapper::toTaskDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Get task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is found",
                content = {@Content(mediaType = "application/jsom",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "404", description = "No such task found", content = @Content)})
    @GetMapping(path = "/{id}")
    public TaskDTO findTaskById(@PathVariable(name = "id") long id) {
        Task existedTask = taskService.getTaskById(id);
        return dataMapper.toTaskDTO(existedTask);
    }

    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    @Operation(summary = "Update the task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task is successfully updated",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @PutMapping(path = "/{id}")
    public TaskDTO updateTask(@RequestBody @Valid TaskDTO dto,
                              @PathVariable(name = "id") long id,
                              @AuthenticationPrincipal UserDetails authDetails,
                              Authentication authentication) {
        Task updatedTask = taskService.updateTask(id, dto, authDetails);
        return dataMapper.toTaskDTO(updatedTask);
    }

    @PreAuthorize(ONLY_AUTHOR_BY_ID)
    @Operation(summary = "Delete the task by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The task has been successfully deleted"),
        @ApiResponse(responseCode = "404", description = "The task is not found", content = @Content)})
    @DeleteMapping(path = "/{id}")
<<<<<<< Updated upstream
    public void deleteTask(@PathVariable(name = "id") long id,
                           @AuthenticationPrincipal UserDetails authDetails) {
        taskService.deleteTask(id, authDetails);
=======
    public void deleteTask(@PathVariable(name = "id") long id) {
        taskService.deleteTask(id);
>>>>>>> Stashed changes
    }
}

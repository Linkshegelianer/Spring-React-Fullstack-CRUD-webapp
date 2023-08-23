package hexlet.code.controller;

import hexlet.code.domain.mapper.TaskStatusModelMapper;
import hexlet.code.domain.dto.TaskStatusRequestDTO;
import hexlet.code.domain.dto.TaskStatusResponseDTO;
import hexlet.code.domain.model.Status;
import hexlet.code.service.StatusService;
import org.springframework.http.HttpStatus;
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

// TODO Validate dto field names (can me extra now)

@RestController
@RequestMapping("${base.url}" + "/statuses")
public class TaskStatusController {

    private final StatusService statusService;
    private final TaskStatusModelMapper statusMapper;

    public TaskStatusController(StatusService statusService,
                                TaskStatusModelMapper statusMapper) {
        this.statusService = statusService;
        this.statusMapper = statusMapper;
    }

    @GetMapping
    public List<TaskStatusResponseDTO> findAllStatuses() {
        List<Status> existedStatuses = statusService.findAllStatuses();
        return existedStatuses.stream()
            .map(statusMapper::toTaskStatusResponseDTO)
            .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public TaskStatusResponseDTO findStatusById(@PathVariable(name = "id") long id) {
        Status existedStatus = statusService.findStatusById(id);
        return statusMapper.toTaskStatusResponseDTO(existedStatus);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusResponseDTO createStatus(@RequestBody @Valid TaskStatusRequestDTO dto) {
        Status createdStatus = statusService.createStatus(dto);
        return statusMapper.toTaskStatusResponseDTO(createdStatus);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusResponseDTO updateStatus(@RequestBody @Valid TaskStatusRequestDTO dto,
                                              @PathVariable(name = "id") long id) {
        Status updatedStatus = statusService.updateStatus(id, dto);
        return statusMapper.toTaskStatusResponseDTO(updatedStatus);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStatus(@PathVariable(name = "id") long id) {
        statusService.deleteStatus(id);
    }
}

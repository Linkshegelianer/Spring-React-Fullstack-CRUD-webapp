package project.code.utils;

import project.code.domain.Task;
import project.code.service.LabelService;
import project.code.service.StatusService;
import project.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class TasksFactory {

    private final StatusService statusService;
    private final LabelService labelService;
    private final UserService userService;

    public TaskBuilder builder(Task task) {
        return new TaskBuilder(statusService, labelService, userService, task);
    }
}

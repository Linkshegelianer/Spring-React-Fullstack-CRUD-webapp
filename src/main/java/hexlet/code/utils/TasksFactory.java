package hexlet.code.utils;

import hexlet.code.domain.model.Task;
import hexlet.code.service.LabelService;
import hexlet.code.service.StatusService;
import hexlet.code.service.UserService;
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

package hexlet.code.domain.builder;

import hexlet.code.domain.model.Task;
import hexlet.code.service.LabelService;
import hexlet.code.service.StatusService;
import hexlet.code.service.UserService;
import org.springframework.stereotype.Component;

@Component
public final class TasksFactory {

    private final StatusService statusService;
    private final LabelService labelService;
    private final UserService userService;

    public TasksFactory(StatusService statusService, LabelService labelService, UserService userService) {
        this.statusService = statusService;
        this.labelService = labelService;
        this.userService = userService;
    }

    public TaskBuilder builder(Task task) {
        return new DefaultTaskBuilder(statusService, labelService, userService, task);
    }
}

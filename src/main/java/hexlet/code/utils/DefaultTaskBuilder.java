package hexlet.code.utils;

import hexlet.code.domain.model.Label;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.Task;
import hexlet.code.domain.model.User;
import hexlet.code.service.LabelService;
import hexlet.code.service.StatusService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DefaultTaskBuilder implements TaskBuilder {

    private final StatusService statusService;
    private final LabelService labelService;
    private final UserService userService;
    private final Task task;

    @Override
    public TaskBuilder setName(String name) {
        this.task.setName(name);
        return this;
    }

    @Override
    public TaskBuilder setDescription(String description) {
        if (description != null) {
            this.task.setDescription(description);
        }
        return this;
    }

    @Override
    public TaskBuilder setTaskStatus(Long taskStatusId) {
        Status status = statusService.getStatusById(taskStatusId);
        this.task.setTaskStatus(status);
        return this;
    }

    @Override
    public TaskBuilder setLabels(List<Long> labelIds) {
        if (labelIds != null) {
            List<Label> labels = labelService.getAllLabelsById(labelIds);
            this.task.setLabels(labels);
        }
        return this;
    }

    @Override
    public TaskBuilder setAuthor(String authorEmail) {
        User author = userService.getUserByEmail(authorEmail);
        this.task.setAuthor(author);
        return this;
    }

    @Override
    public TaskBuilder setExecutor(Long executorId) {
        if (executorId != null) {
            User executor = userService.getUserById(executorId);
            this.task.setExecutor(executor);
        }
        return this;
    }

    @Override
    public Task build() {
        return this.task;
    }
}

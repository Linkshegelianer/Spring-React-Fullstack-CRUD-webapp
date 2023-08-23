package hexlet.code.domain.builder;

import hexlet.code.domain.model.Task;

import java.util.List;

public interface TaskBuilder {

    TaskBuilder setName(String name);
    TaskBuilder setDescription(String description);
    TaskBuilder setTaskStatus(Long taskStatusId);
    TaskBuilder setLabels(List<Long> labelIds);
    TaskBuilder setAuthor(String authorEmail);
    TaskBuilder setExecutor(Long executorId);
    Task build();
}

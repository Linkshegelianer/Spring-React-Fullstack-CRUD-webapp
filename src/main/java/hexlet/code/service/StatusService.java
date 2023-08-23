package hexlet.code.service;

import hexlet.code.domain.mapper.TaskStatusModelMapper;
import hexlet.code.domain.dto.TaskStatusRequestDTO;
import hexlet.code.domain.model.Status;
import hexlet.code.exception.NotFoundException;
import hexlet.code.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface StatusService {

    public List<Status> findAllStatuses();

    public Status findStatusById(long id);

    public Status getStatusReferenceById(long id);

    @Transactional
    public Status createStatus(TaskStatusRequestDTO dto);

    @Transactional
    public Status updateStatus(long id, TaskStatusRequestDTO dto);

    @Transactional
    public void deleteStatus(long id);
}

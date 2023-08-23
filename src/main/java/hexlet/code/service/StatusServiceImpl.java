package hexlet.code.service;

import hexlet.code.domain.dto.TaskStatusRequestDTO;
import hexlet.code.domain.mapper.TaskStatusModelMapper;
import hexlet.code.domain.model.Status;
import hexlet.code.exception.NotFoundException;
import hexlet.code.repository.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final TaskStatusModelMapper statusMapper;

    public StatusServiceImpl(StatusRepository statusRepository,
                         TaskStatusModelMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }

    public List<Status> findAllStatuses() {
        return statusRepository.findAllByOrderByIdAsc();
    }

    public Status findStatusById(long id) {
        return statusRepository.findTaskStatusById(id)
                .orElseThrow(() -> new NotFoundException("Status with id='%d' not found!".formatted(id)));
    }

    public Status getStatusReferenceById(long id) {
        return statusRepository.getReferenceById(id);
    }

    @Transactional
    public Status createStatus(TaskStatusRequestDTO dto) {
        Status newStatus = statusMapper.toTaskStatusModel(dto);
        return statusRepository.save(newStatus);
    }

    @Transactional
    public Status updateStatus(long id, TaskStatusRequestDTO dto) {
        Status statusToUpdate = findStatusById(id);
        statusToUpdate.setName(dto.getName());
        return statusToUpdate;
    }

    @Transactional
    public void deleteStatus(long id) {
        Status existedStatus = findStatusById(id);
        statusRepository.delete(existedStatus);
    }
}

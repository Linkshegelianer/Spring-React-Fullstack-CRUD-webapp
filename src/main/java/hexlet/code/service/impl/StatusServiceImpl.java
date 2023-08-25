package hexlet.code.service.impl;

import hexlet.code.domain.dto.StatusRequestDTO;
import hexlet.code.domain.mapper.StatusModelMapper;
import hexlet.code.domain.model.Status;
import hexlet.code.repository.StatusRepository;
import hexlet.code.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final StatusModelMapper statusMapper;

    @Transactional
    public Status createStatus(StatusRequestDTO dto) {
        Status newStatus = statusMapper.toTaskStatusModel(dto);
        return statusRepository.save(newStatus);
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAllByOrderByIdAsc();
    }

    public Status getStatusById(long id) {
        return statusRepository.findTaskStatusById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status with id='%d' not found!".formatted(id)));
    }

    public Status getStatusReferenceById(long id) {
        return statusRepository.getReferenceById(id);
    }

    @Transactional
    public Status updateStatus(long id, StatusRequestDTO dto) {
        Status statusToUpdate = getStatusById(id);
        statusToUpdate.setName(dto.getName());
        return statusToUpdate;
    }

    @Transactional
    public void deleteStatus(long id) {
        Status existedStatus = getStatusById(id);
        statusRepository.delete(existedStatus);
    }
}

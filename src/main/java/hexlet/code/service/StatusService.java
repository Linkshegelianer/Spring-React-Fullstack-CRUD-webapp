package hexlet.code.service;

import hexlet.code.domain.dto.StatusRequestDTO;
import hexlet.code.domain.model.Status;
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
    public Status createStatus(StatusRequestDTO dto);

    @Transactional
    public Status updateStatus(long id, StatusRequestDTO dto);

    @Transactional
    public void deleteStatus(long id);
}

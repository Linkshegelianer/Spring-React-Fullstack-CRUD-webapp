package hexlet.code.service;

import hexlet.code.domain.dto.StatusRequestDTO;
import hexlet.code.domain.model.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface StatusService {

    @Transactional
    public Status createStatus(StatusRequestDTO dto);

    public List<Status> getAllStatuses();

    public Status getStatusById(long id);

    public Status getStatusReferenceById(long id);

    @Transactional
    public Status updateStatus(long id, StatusRequestDTO dto);

    @Transactional
    public void deleteStatus(long id);
}

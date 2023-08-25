package hexlet.code.service;

import hexlet.code.domain.dto.StatusDTO;
import hexlet.code.domain.model.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface StatusService {

    @Transactional
    public Status createStatus(StatusDTO dto);

    public List<Status> getAllStatuses();

    public Status getStatusById(long id);

    public Status getStatusReferenceById(long id);

    @Transactional
    public Status updateStatus(long id, StatusDTO dto);

    @Transactional
    public void deleteStatus(long id);
}

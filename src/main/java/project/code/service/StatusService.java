package project.code.service;

import project.code.dto.StatusDTO;
import project.code.domain.Status;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface StatusService {

    @Transactional
    Status createStatus(StatusDTO dto);

    List<Status> getAllStatuses();

    Status getStatusById(long id);

    Status getStatusReferenceById(long id);

    @Transactional
    Status updateStatus(long id, StatusDTO dto);

    @Transactional
    void deleteStatus(long id);
}

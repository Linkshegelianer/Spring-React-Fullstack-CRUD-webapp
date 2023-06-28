package hexlet.code.service;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;

import java.util.List;

public interface StatusService {

    Status createNewStatus(StatusDto statusDto);

    Status updateStatus(long id, StatusDto statusDto);

    Status findStatusById(long id);

    List<Status> findAllStatuses();

    void deleteStatus(long id);
}

package hexlet.code.service;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;
import hexlet.code.model.User;
import hexlet.code.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status createNewStatus(StatusDto statusDto) {
        final Status status = new Status();
        return statusRepository.save(status);
    }

    @Override
    public Status updateStatus(long id, StatusDto statusDto) {
        Status statusToUpdate = findStatusById(id);
        statusToUpdate.setName(statusDto.getName());
        return statusToUpdate;
    }

    @Override
    public Status findStatusById(long id) throws ObjectNotFoundException {
        if (id != null) {
            return statusRepository.findStatusById(id)
                    .orElseThrow(() -> new ObjectNotFoundException());
        }
        return null;
    }

    @Override
    public List<Status> findAllStatuses() {
        return statusRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void deleteStatus(long id) {
        Status existedStatus = findStatusById(id);
        statusRepository.delete(existedStatus);
    }
}

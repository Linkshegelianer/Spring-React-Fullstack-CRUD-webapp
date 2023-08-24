package hexlet.code.domain.mapper;

import hexlet.code.domain.dto.StatusRequestDTO;
import hexlet.code.domain.dto.StatusResponseDTO;
import hexlet.code.domain.model.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusModelMapper {

    public StatusResponseDTO toTaskStatusResponseDTO(final Status status) {
        return new StatusResponseDTO(
            status.getId(),
            status.getName(),
            status.getCreatedAt()
        );
    }

    public Status toTaskStatusModel(final StatusRequestDTO dto) {
        return new Status(
            dto.getName()
        );
    }
}

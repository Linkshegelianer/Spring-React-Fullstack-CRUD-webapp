package hexlet.code.domain.mapper;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.dto.LabelResponseDTO;
import hexlet.code.domain.model.Label;
import org.springframework.stereotype.Component;

@Component
public class LabelModelMapper {

    public LabelResponseDTO toLabelResponseDTO(final Label label) {
        return new LabelResponseDTO(
            label.getId(),
            label.getName(),
            label.getCreatedAt()
        );
    }

    public Label toLabelModel(final LabelRequestDTO dto) {
        return new Label(
            dto.getName()
        );
    }
}

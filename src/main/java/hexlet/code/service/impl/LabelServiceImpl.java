package hexlet.code.service.impl;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.mapper.LabelModelMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelModelMapper labelMapper;

    @Transactional
    public Label createLabel(LabelRequestDTO dto) {
        Label newLabel = labelMapper.toLabelModel(dto);
        return labelRepository.save(newLabel);
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAllByOrderByIdAsc();
    }

    public List<Label> getAllLabelsById(List<Long> labelIds) {
        if (labelIds != null) {
            return labelRepository.findAllById(labelIds);
        }
        return Collections.emptyList();
    }

    public List<Label> getAllLabelReferencesById(List<Long> labelIds) {
        return labelIds.stream()
                .map(labelRepository::getReferenceById)
                .collect(Collectors.toList());
    }

    public Label getLabelById(long id) {
        return labelRepository.findLabelById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label with id='%d' not found!".formatted(id)));
    }

    @Transactional
    public Label updateLabel(long id, LabelRequestDTO dto) {
        Label labelToUpdate = getLabelById(id);
        labelToUpdate.setName(dto.getName());
        return labelToUpdate;
    }

    @Transactional
    public void deleteLabel(long id) {
        Label existedLabel = getLabelById(id);
        labelRepository.delete(existedLabel);
    }
}

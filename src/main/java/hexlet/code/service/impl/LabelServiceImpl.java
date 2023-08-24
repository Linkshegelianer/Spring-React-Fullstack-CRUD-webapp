package hexlet.code.service.impl;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.mapper.LabelModelMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelModelMapper labelMapper;

    public LabelServiceImpl(LabelRepository labelRepository,
                        LabelModelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    public List<Label> findAllLabels() {
        return labelRepository.findAllByOrderByIdAsc();
    }

    public List<Label> findAllLabelsById(List<Long> labelIds) {
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

    public Label findLabelById(long id) {
        return labelRepository.findLabelById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label with id='%d' not found!".formatted(id)));
    }

    @Transactional
    public Label createLabel(LabelRequestDTO dto) {
        Label newLabel = labelMapper.toLabelModel(dto);
        return labelRepository.save(newLabel);
    }

    @Transactional
    public Label updateLabel(long id, LabelRequestDTO dto) {
        Label labelToUpdate = findLabelById(id);
        labelToUpdate.setName(dto.getName());
        return labelToUpdate;
    }

    @Transactional
    public void deleteLabel(long id) {
        Label existedLabel = findLabelById(id);
        labelRepository.delete(existedLabel);
    }
}

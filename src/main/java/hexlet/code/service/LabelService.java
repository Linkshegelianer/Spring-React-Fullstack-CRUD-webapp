package hexlet.code.service;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.mapper.LabelModelMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.exception.NotFoundException;
import hexlet.code.repository.LabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public interface LabelService {

    public List<Label> findAllLabels();


    public List<Label> findAllLabelsById(List<Long> labelIds);


    public List<Label> getAllLabelReferencesById(List<Long> labelIds);

    public Label findLabelById(long id);

    @Transactional
    public Label createLabel(LabelRequestDTO dto);

    @Transactional
    public Label updateLabel(long id, LabelRequestDTO dto);

    @Transactional
    public void deleteLabel(long id);
}

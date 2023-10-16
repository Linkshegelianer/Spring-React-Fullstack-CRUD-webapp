package project.code.service;

import project.code.dto.LabelDTO;
import project.code.domain.Label;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LabelService {

    @Transactional
    Label createLabel(LabelDTO dto);

    List<Label> getAllLabels();

    List<Label> getAllLabelsById(List<Long> labelIds);

    List<Label> getAllLabelReferencesById(List<Long> labelIds);

    Label getLabelById(long id);

    @Transactional
    Label updateLabel(long id, LabelDTO dto);

    @Transactional
    void deleteLabel(long id);
}

package hexlet.code.service;

import hexlet.code.domain.dto.LabelDTO;
import hexlet.code.domain.model.Label;
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

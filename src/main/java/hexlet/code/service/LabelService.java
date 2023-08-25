package hexlet.code.service;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.model.Label;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface LabelService {

    @Transactional
    public Label createLabel(LabelRequestDTO dto);

    public List<Label> getAllLabels();

    public List<Label> getAllLabelsById(List<Long> labelIds);

    public List<Label> getAllLabelReferencesById(List<Long> labelIds);

    public Label getLabelById(long id);

    @Transactional
    public Label updateLabel(long id, LabelRequestDTO dto);

    @Transactional
    public void deleteLabel(long id);
}

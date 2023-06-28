package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {

    Label createNewLabel(LabelDto labelDto);

    Label updateLabel(long id, LabelDto labelDto);

    Label findLabelById(long id);

    List<Label> findAllLabels();

    void deleteLabel(long id);
}

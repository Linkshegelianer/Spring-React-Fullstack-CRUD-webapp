package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label createNewLabel(LabelDto labelDto) {
        final Label label = new Label();
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDto) {
        Label labelToUpdate = findLabelById(id);
        labelToUpdate.setName(labelDto.getName());
        return labelToUpdate;
    }

    @Override
    public Label findLabelById(long id) throws ObjectNotFoundException {
        if (id != null) {
            return labelRepository.findLabelById(id)
                    .orElseThrow(() -> new ObjectNotFoundException());
        }
        return null;
    }

    @Override
    public List<Label> findAllLabels() {
        return labelRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void deleteLabel(long id) {
        Label existedLabel = findLabelById(id);
        labelRepository.delete(existedLabel);
    }
}

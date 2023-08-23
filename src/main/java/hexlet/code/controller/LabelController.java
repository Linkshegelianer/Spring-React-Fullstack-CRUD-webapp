package hexlet.code.controller;

import hexlet.code.domain.dto.LabelRequestDTO;
import hexlet.code.domain.dto.LabelResponseDTO;
import hexlet.code.domain.mapper.LabelModelMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${base.url}" + "/labels")
public class LabelController {

    private final LabelService labelService;
    private final LabelModelMapper labelMapper;

    public LabelController(LabelService labelService,
                           LabelModelMapper labelMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
    }

    @GetMapping
    public List<LabelResponseDTO> findAllLabels() {
        List<Label> existedLabels = labelService.findAllLabels();
        return existedLabels.stream()
            .map(labelMapper::toLabelResponseDTO)
            .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public LabelResponseDTO findLabelById(@PathVariable(name = "id") long id) {
        Label existedLabel = labelService.findLabelById(id);
        return labelMapper.toLabelResponseDTO(existedLabel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponseDTO createLabel(@RequestBody @Valid LabelRequestDTO dto) {
        Label createdLabel = labelService.createLabel(dto);
        return labelMapper.toLabelResponseDTO(createdLabel);
    }

    @PutMapping(path = "/{id}")
    public LabelResponseDTO updateLabel(@RequestBody @Valid LabelRequestDTO dto,
                                        @PathVariable(name = "id") long id) {
        Label updatedLabel = labelService.updateLabel(id, dto);
        return labelMapper.toLabelResponseDTO(updatedLabel);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteLabel(@PathVariable(name = "id") long id) {
        labelService.deleteLabel(id);
    }
}

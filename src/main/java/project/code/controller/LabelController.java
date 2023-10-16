package project.code.controller;

import project.code.dto.LabelDTO;
import project.code.domain.Label;
import project.code.utils.DataMapper;
import project.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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

import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base.url}" + "/labels")
public class LabelController {

    private final LabelService labelService;
    private final DataMapper dataMapper;

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The label has been successfully created",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class))})})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO createLabel(@RequestBody @Valid LabelDTO dto) {
        Label createdLabel = labelService.createLabel(dto);
        return dataMapper.toLabelDTO(createdLabel);
    }

    @Operation(summary = "Get all labels")
    @ApiResponse(responseCode = "200", description = "All labels are found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    @GetMapping
    public List<LabelDTO> findAllLabels() {
        List<Label> existedLabels = labelService.getAllLabels();

        return existedLabels.stream()
            .map(dataMapper::toLabelDTO)
            .collect(Collectors.toList());
    }

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The label is found",
                content = {@Content(mediaType = "application/jsom",
                        schema = @Schema(implementation = Label.class))}),
        @ApiResponse(responseCode = "404", description = "No such label found", content = @Content)})
    @GetMapping(path = "/{id}")
    public LabelDTO findLabelById(@PathVariable(name = "id") long id) {
        Label existedLabel = labelService.getLabelById(id);
        return dataMapper.toLabelDTO(existedLabel);
    }

    @Operation(summary = "Update the label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The label is updated",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class))}),
        @ApiResponse(responseCode = "422", description = "Invalid request", content = @Content)})
    @PutMapping(path = "/{id}")
    public LabelDTO updateLabel(@RequestBody @Valid LabelDTO dto,
                                        @PathVariable(name = "id") long id) {
        Label updatedLabel = labelService.updateLabel(id, dto);
        return dataMapper.toLabelDTO(updatedLabel);
    }

    @Operation(summary = "Delete the label by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Label has been successfully deleted"),
        @ApiResponse(responseCode = "404", description = "No such label found")})
    @DeleteMapping(path = "/{id}")
    public void deleteLabel(@PathVariable(name = "id") long id) {
        labelService.deleteLabel(id);
    }
}

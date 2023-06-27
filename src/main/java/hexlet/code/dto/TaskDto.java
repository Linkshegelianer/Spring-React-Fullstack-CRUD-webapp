package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public final class TaskDto {

    private long id;

    @NotBlank(message = "Field 'name' must not be empty!")
    private String name;

    private String description;

    private long executorId;

    private long authorId;

    @NotNull(message = "Field 'taskStatusId' must not be null!")
    private Long taskStatusId;


    private List<Long> labelList;

}

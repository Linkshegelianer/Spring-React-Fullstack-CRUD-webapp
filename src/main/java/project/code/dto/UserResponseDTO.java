package project.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;
}

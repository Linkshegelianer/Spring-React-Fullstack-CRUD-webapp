package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;

    private Instant createdAt;

    @NotBlank(message = "Field 'email' must not be empty!")
    @Email(message = "Email format is invalid!")
    private String email;

    @NotBlank(message = "Field 'firstName' must not be empty!")
    private String firstName;

    @NotBlank(message = "Field 'lastName' must not be empty!")
    private String lastName;

    @NotBlank(message = "Field 'password' must not be empty!")
    @Size(min = 3, max = 100)
    private String password;

}

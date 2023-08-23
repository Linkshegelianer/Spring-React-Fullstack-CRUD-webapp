package hexlet.code.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRequestDTO {

    @NotBlank(message = "Field 'email' must not be empty!")
    @Email(message = "Email format is invalid!")
    private String email;

    @NotBlank(message = "Field 'firstName' must not be empty!")
    private String firstName;

    @NotBlank(message = "Field 'lastName' must not be empty!")
    private String lastName;

    @NotBlank(message = "Field 'password' must not be empty!")
    @Size(min = 3, message = "Password length must be at least 3 characters!")
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}

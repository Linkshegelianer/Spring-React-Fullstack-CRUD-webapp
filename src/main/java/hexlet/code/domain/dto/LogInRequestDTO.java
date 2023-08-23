package hexlet.code.domain.dto;

public class LogInRequestDTO {

    private String email;
    private String password;

    public LogInRequestDTO() {

    }

    public LogInRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

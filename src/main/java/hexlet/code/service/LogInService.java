package hexlet.code.service;

import hexlet.code.domain.dto.LogInRequestDTO;

public interface LogInService {

    public String authenticate(LogInRequestDTO logInDTO);
}

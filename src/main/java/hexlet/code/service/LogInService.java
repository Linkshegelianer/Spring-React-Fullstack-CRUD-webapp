package hexlet.code.service;

import hexlet.code.domain.dto.LogInDTO;

public interface LogInService {

    public String authenticate(LogInDTO logInDTO);
}

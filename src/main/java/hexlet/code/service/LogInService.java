package hexlet.code.service;

import hexlet.code.domain.dto.LogInDTO;

public interface LogInService {

    String authenticate(LogInDTO logInDTO);
}

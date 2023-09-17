package hexlet.code.service;

import hexlet.code.dto.LogInDTO;

public interface LogInService {

    String authenticate(LogInDTO logInDTO);
}

package project.code.service;

import project.code.dto.LogInDTO;

public interface LogInService {

    String authenticate(LogInDTO logInDTO);
}

package hexlet.code.controller;

import hexlet.code.domain.dto.LogInRequestDTO;
import hexlet.code.service.impl.LogInServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${base.url}")
public class LogInController {

    private final LogInServiceImpl logInService;

    @PostMapping(path = "/login")
    public String logIn(@RequestBody LogInRequestDTO dto) {
        return logInService.authenticate(dto);
    }
}

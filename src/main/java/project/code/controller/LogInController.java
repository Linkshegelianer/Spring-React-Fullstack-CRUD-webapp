package project.code.controller;

import project.code.dto.LogInDTO;
import project.code.service.impl.LogInServiceImpl;
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
    public String logIn(@RequestBody LogInDTO dto) {
        return logInService.authenticate(dto);
    }
}

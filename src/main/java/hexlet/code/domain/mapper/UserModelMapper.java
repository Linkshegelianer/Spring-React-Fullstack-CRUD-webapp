package hexlet.code.domain.mapper;

import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.dto.UserResponseDTO;
import hexlet.code.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {

    public UserResponseDTO toUserResponseDTO(final User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getCreatedAt()
        );
    }

    public User toUserModel(final UserRequestDTO dto) {
        return new User(
            dto.getFirstName(),
            dto.getLastName(),
            dto.getEmail().toLowerCase(),
            dto.getPassword()
        );
    }
}

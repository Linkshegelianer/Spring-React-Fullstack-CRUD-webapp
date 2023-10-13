package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.domain.Status;
import hexlet.code.domain.User;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper().findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String DEFAULT_EMAIL = "default@mail.com";
    private static final String DEFAULT_FIRST_NAME = "First Name";
    private static final String DEFAULT_LAST_NAME = "Last Name";
    private static final String DEFAULT_PASSWORD = "password";
    public static final String TOKEN_PREFIX = "Bearer";

    public User buildDefaultUser(String email) {
        User user = new User();
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setEmail(email);
        user.setPassword(DEFAULT_PASSWORD);
        return user;
    }

    public User buildDefaultUser() {
        return buildDefaultUser(DEFAULT_EMAIL);
    }

    public Status buildDefaultStatus(String name) {
        Status status = new Status();
        status.setName(name);
        return status;
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return JSON_MAPPER.readValue(json, to);
    }

    public static String asJson(final Object object) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(object);
    }
}

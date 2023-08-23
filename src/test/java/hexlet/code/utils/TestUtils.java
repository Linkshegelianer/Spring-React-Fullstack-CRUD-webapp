package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper().findAndRegisterModules();

    private static final String DEFAULT_EMAIL = "default@testmail.com";
    private static final String DEFAULT_FIRST_NAME = "DEFAULT_FirstName";
    private static final String DEFAULT_LAST_NAME = "DEFAULT_LastName";
    private static final String DEFAULT_PASS = "pass";
    public static final String TOKEN_PREFIX = "Bearer ";

    public String toJson(Object object) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(object);
    }

    public  <T> T jsonToObject(String json, TypeReference<T> type) throws JsonProcessingException {
        return JSON_MAPPER.readValue(json, type);
    }

    public User buildDefaultUser(String email) {
        User user = new User();
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setEmail(email);
        user.setPassword(DEFAULT_PASS);
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
}

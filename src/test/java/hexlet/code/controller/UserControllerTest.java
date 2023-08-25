package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.domain.dto.LogInRequestDTO;
import hexlet.code.domain.dto.UserRequestDTO;
import hexlet.code.domain.dto.UserResponseDTO;
import hexlet.code.domain.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static hexlet.code.utils.TestUtils.TOKEN_PREFIX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String TEST_EMAIL_1 = "email.1@testmail.com";
    private static final String TEST_EMAIL_2 = "email.2@testmail.com";
    private static final String TEST_FIRST_NAME = "First Name";
    private static final String TEST_LAST_NAME = "Last Name";
    private static final String TEST_UPDATED_LAST_NAME = "Updated Last Name";
    private static final String TEST_PASSWORD = "password";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestUtils utils;

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() throws Exception {
        long expectedCountInDB = 0;
        long actualCount = userRepository.count();

        assertEquals(expectedCountInDB, actualCount);

        registerUser(TEST_EMAIL_1)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(TEST_EMAIL_1)));

        expectedCountInDB = 1;
        actualCount = userRepository.count();

        assertEquals(expectedCountInDB, actualCount);
    }

    @Test
    void testFindAllUsers() throws Exception {
        registerUser(TEST_EMAIL_1)
            .andExpect(status().isCreated());

        registerUser(TEST_EMAIL_2)
            .andExpect(status().isCreated());

        MockHttpServletResponse response = mvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        List<UserResponseDTO> userDTOList = utils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );
        int expectedDtoCount = 2;
        int actual = userDTOList.size();

        assertEquals(expectedDtoCount, actual);
        assertEquals(TEST_EMAIL_1, userDTOList.get(0).getEmail());
        assertEquals(TEST_EMAIL_2, userDTOList.get(1).getEmail());
    }

    @Test
    void testFindUserById() throws Exception {
        registerUser(TEST_EMAIL_1)
            .andExpect(status().isCreated());

        MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
            .andExpect(status().isOk())
            .andReturn().getResponse();

        String jwtToken = signInResponse.getContentAsString();

        User existedUser = userRepository.findAll().get(0);
        long userId = existedUser.getId();

        MockHttpServletResponse response = mvc.perform(get("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        UserResponseDTO userDTO = utils.jsonToObject(response.getContentAsString(UTF_8), new TypeReference<>() { });

        assertEquals(existedUser.getId(), userDTO.getId());
        assertEquals(existedUser.getEmail(), userDTO.getEmail());
        assertEquals(existedUser.getFirstName(), userDTO.getFirstName());
        assertEquals(existedUser.getLastName(), userDTO.getLastName());
    }

    @Test
    void testUpdateUser() throws Exception {
        registerUser(TEST_EMAIL_1)
            .andExpect(status().isCreated());

        MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
            .andExpect(status().isOk())
            .andReturn().getResponse();

        String jwtToken = signInResponse.getContentAsString(UTF_8);

        User userToUpdate = userRepository.findAll().get(0);
        String previousLastName = userToUpdate.getLastName();
        UserRequestDTO dto = buildUpdateLastNameDTO(TEST_EMAIL_1, TEST_UPDATED_LAST_NAME);
        long userId = userToUpdate.getId();

        mvc.perform(put("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken)
                .content(utils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()) // here
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(userId), Long.class))
            .andExpect(jsonPath("$.lastName", is(TEST_UPDATED_LAST_NAME)));

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findUserByLastName(previousLastName).orElse(null));
        assertNotNull(userRepository.findUserByLastName(TEST_UPDATED_LAST_NAME).orElse(null));
    }

    @Test
    void testDeleteUser() throws Exception {
        registerUser(TEST_EMAIL_1)
            .andExpect(status().isCreated());

        MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
            .andExpect(status().isOk())
            .andReturn().getResponse();

        String jwtToken = signInResponse.getContentAsString();

        User userToDelete = userRepository.findAll().get(0);
        long userId = userToDelete.getId();

        mvc.perform(delete("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken))
            .andExpect(status().isOk());

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    void testUpdateOrDeleteUserByNotTheOwner() throws Exception { // ok
        registerUser(TEST_EMAIL_1)
            .andExpect(status().isCreated());

        registerUser(TEST_EMAIL_2)
            .andExpect(status().isCreated());

        MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_2)
            .andExpect(status().isOk())
            .andReturn().getResponse();

        String jwtToken = signInResponse.getContentAsString(UTF_8);

        User userToUpdateOrDelete = userRepository.findAll().get(0);
        long userId = userToUpdateOrDelete.getId();
        UserRequestDTO dto = buildUpdateLastNameDTO(TEST_EMAIL_1, TEST_UPDATED_LAST_NAME);

        mvc.perform(put("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken)
                .content(utils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        mvc.perform(delete("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken))
            .andExpect(status().isForbidden());
    }

    private ResultActions registerUser(String email) throws Exception {
        UserRequestDTO userRequestDTO = new UserRequestDTO(
            email,
            TEST_FIRST_NAME,
            TEST_LAST_NAME,
                TEST_PASSWORD
        );

        return mvc.perform(post("/api/users")
            .content(utils.toJson(userRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions signIn(String email) throws Exception {
        LogInRequestDTO logInRequestDTO = new LogInRequestDTO(email, TEST_PASSWORD);

        return mvc.perform(post("/api/login")
            .content(utils.toJson(logInRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }

    private UserRequestDTO buildUpdateLastNameDTO(String email, String newLastName) {
        return new UserRequestDTO(
            email,
            TEST_FIRST_NAME,
            newLastName,
                TEST_PASSWORD
        );
    }
}

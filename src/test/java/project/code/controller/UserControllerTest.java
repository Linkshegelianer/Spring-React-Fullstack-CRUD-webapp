package project.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import project.code.dto.LogInDTO;
import project.code.dto.UserRequestDTO;
import project.code.dto.UserResponseDTO;
import project.code.domain.User;
import project.code.repository.TaskRepository;
import project.code.repository.StatusRepository;
import project.code.repository.UserRepository;
import project.code.utils.TestUtils;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static project.code.utils.TestUtils.TOKEN_PREFIX;
import static project.code.utils.TestUtils.fromJson;
import static java.nio.charset.StandardCharsets.UTF_8;
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
        final ResultActions creationResult = registerUser(TEST_EMAIL_1)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final Long userId = extractUserIdFromResponse(creationResult);
        final Optional<User> actual = userRepository.findUserById(userId);

        assertTrue(userRepository.existsById(userId));
        assertEquals(TEST_EMAIL_1, actual.map(User::getEmail).orElse(null));
    }

    @Test
    void testFindAllUsers() throws Exception {
        registerUser(TEST_EMAIL_1).andExpect(status().isCreated());

        registerUser(TEST_EMAIL_2).andExpect(status().isCreated());

        final MockHttpServletResponse response = mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final List<UserResponseDTO> userDTOList =
                utils.fromJson(response.getContentAsString(), new TypeReference<>() { });
        final List<User> expected = userRepository.findAll();

        assertEquals(expected.size(), userDTOList.size());

        final Set<String> expectedEmails = expected.stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        for (UserResponseDTO userDTO : userDTOList) {
            assertTrue(expectedEmails.contains(userDTO.getEmail()));
        }
    }

    @Test
    void testFindUserById() throws Exception {
        registerUser(TEST_EMAIL_1).andExpect(status().isCreated());

        final MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String jwtToken = signInResponse.getContentAsString();

        final User existedUser = userRepository.findAll().get(0);
        final long userId = existedUser.getId();

        final MockHttpServletResponse response = mvc.perform(get("/api/users/%d".formatted(userId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final UserResponseDTO userDTO = utils.fromJson(response.getContentAsString(UTF_8), new TypeReference<>() { });

        assertEquals(existedUser.getId(), userDTO.getId());
        assertEquals(existedUser.getEmail(), userDTO.getEmail());
        assertEquals(existedUser.getFirstName(), userDTO.getFirstName());
        assertEquals(existedUser.getLastName(), userDTO.getLastName());
    }

    @Test
    void testUpdateUser() throws Exception {
        registerUser(TEST_EMAIL_1).andExpect(status().isCreated());

        final MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String jwtToken = signInResponse.getContentAsString(UTF_8);

        final User userToUpdate = userRepository.findAll().get(0);
        final String previousLastName = userToUpdate.getLastName();
        final UserRequestDTO dto = buildUpdateLastNameDTO(TEST_EMAIL_1, TEST_UPDATED_LAST_NAME);
        final long userId = userToUpdate.getId();

        final MockHttpServletResponse response = mvc.perform(put("/api/users/%d".formatted(userId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken)
                .content(utils.asJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        final long id = fromJson(response.getContentAsString(), new TypeReference<User>() { }).getId();
        final Optional<User> actual = userRepository.findUserById(id);

        assertTrue(userRepository.existsById(id));
        assertNotNull(actual.orElse(null));
        assertNull(userRepository.findUserByLastName(previousLastName).orElse(null));
        assertNotNull(userRepository.findUserByLastName(TEST_UPDATED_LAST_NAME).orElse(null));
    }

    @Test
    void testDeleteUser() throws Exception {
        registerUser(TEST_EMAIL_1).andExpect(status().isCreated());

        final MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_1)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String jwtToken = signInResponse.getContentAsString();

        final User userToDelete = userRepository.findAll().get(0);
        final long userId = userToDelete.getId();

        mvc.perform(delete("/api/users/%d".formatted(userId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken))
                .andExpect(status().isOk());

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    void testUpdateOrDeleteUserByNotTheOwner() throws Exception {
        registerUser(TEST_EMAIL_1).andExpect(status().isCreated());
        registerUser(TEST_EMAIL_2).andExpect(status().isCreated());

        final MockHttpServletResponse signInResponse = signIn(TEST_EMAIL_2)
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String jwtToken = signInResponse.getContentAsString(UTF_8);

        final User userToUpdateOrDelete = userRepository.findAll().get(0);
        final long userId = userToUpdateOrDelete.getId();
        final UserRequestDTO dto = buildUpdateLastNameDTO(TEST_EMAIL_1, TEST_UPDATED_LAST_NAME);

        mvc.perform(put("/api/users/%d".formatted(userId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + jwtToken)
                        .content(utils.asJson(dto))
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
                .content(utils.asJson(userRequestDTO))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions signIn(String email) throws Exception {
        LogInDTO logInRequestDTO = new LogInDTO(email, TEST_PASSWORD);

        return mvc.perform(post("/api/login")
                .content(utils.asJson(logInRequestDTO))
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

    private Long extractUserIdFromResponse(ResultActions resultActions) throws Exception {
        MvcResult result = resultActions.andReturn();
        String responseContent = result.getResponse().getContentAsString();
        TypeReference<User> typeReference = new TypeReference<User>() { };
        User user = fromJson(responseContent, typeReference);
        return user.getId();
    }
}

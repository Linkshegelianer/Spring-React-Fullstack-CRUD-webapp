package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.domain.dto.TaskStatusRequestDTO;
import hexlet.code.domain.dto.TaskStatusResponseDTO;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JWTUtils;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static hexlet.code.utils.TestUtils.TOKEN_PREFIX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // Test with a real http server
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureMockMvc
class StatusControllerTest {

    private static final String TEST_STATUS_NAME_1 = "Новая";
    private static final String TEST_STATUS_NAME_2 = "В работе";
    private static final String TEST_UPDATED_STATUS_NAME = "Custom status";
    private String token;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    void beforeEach() {
        User testUser = userRepository.save(testUtils.buildDefaultUser());
        token = jwtUtils.generateToken(testUser);
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAllStatuses() throws Exception {
        createStatus(TEST_STATUS_NAME_1)
            .andExpect(status().isCreated());

        createStatus(TEST_STATUS_NAME_2)
            .andExpect(status().isCreated());

        MockHttpServletResponse response = mvc.perform(get("/api/statuses"))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        List<TaskStatusResponseDTO> statusDTOList = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );
        int expectedDtoCount = 2;
        int actual = statusDTOList.size();

        assertEquals(expectedDtoCount, actual);
        assertEquals(TEST_STATUS_NAME_1, statusDTOList.get(0).getName());
        assertEquals(TEST_STATUS_NAME_2, statusDTOList.get(1).getName());
    }

    @Test
    void testFindStatusById() throws Exception {
        createStatus(TEST_STATUS_NAME_1)
            .andExpect(status().isCreated());

        Status existedStatus = statusRepository.findAll().get(0);
        long statusId = existedStatus.getId();

        MockHttpServletResponse response = mvc.perform(get("/api/statuses/%d".formatted(statusId)))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        TaskStatusResponseDTO statusDTO = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );

        assertEquals(existedStatus.getId(), statusDTO.getId());
        assertEquals(existedStatus.getName(), statusDTO.getName());
    }

    @Test
    void testCreateStatus() throws Exception {
        long expectedCountInDB = 0;
        long actualCount = statusRepository.count();

        assertEquals(expectedCountInDB, actualCount);

        createStatus(TEST_STATUS_NAME_1)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", is(TEST_STATUS_NAME_1)));

        expectedCountInDB = 1;
        actualCount = statusRepository.count();

        assertEquals(expectedCountInDB, actualCount);
    }

    @Test
    void testUpdateStatus() throws Exception {
        createStatus(TEST_STATUS_NAME_1)
            .andExpect(status().isCreated());

        Status statusToUpdate = statusRepository.findAll().get(0);
        long statusId = statusToUpdate.getId();
        TaskStatusRequestDTO dto = new TaskStatusRequestDTO(TEST_UPDATED_STATUS_NAME);

        mvc.perform(put("/api/statuses/%d".formatted(statusId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                .content(testUtils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(statusId), Long.class))
            .andExpect(jsonPath("$.name", is(TEST_UPDATED_STATUS_NAME)));

        Optional<Status> actual = statusRepository.findTaskStatusById(statusId);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_STATUS_NAME, actual.get().getName());
    }

    @Test
    void testDeleteStatus() throws Exception {
        createStatus(TEST_STATUS_NAME_1)
            .andExpect(status().isCreated());

        Status statusToDelete = statusRepository.findAll().get(0);
        long statusId = statusToDelete.getId();

        mvc.perform(delete("/api/statuses/%d".formatted(statusId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
            .andExpect(status().isOk());

        assertFalse(statusRepository.existsById(statusId));
    }

    private ResultActions createStatus(String name) throws Exception {
        TaskStatusRequestDTO statusRequestDTO = new TaskStatusRequestDTO(name);

        return mvc.perform(post("/api/statuses")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
            .content(testUtils.toJson(statusRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }
}

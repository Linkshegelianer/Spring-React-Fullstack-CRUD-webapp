package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.dto.StatusDTO;
import hexlet.code.domain.Status;
import hexlet.code.domain.User;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static hexlet.code.utils.TestUtils.TOKEN_PREFIX;
import static hexlet.code.utils.TestUtils.fromJson;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureMockMvc
class StatusControllerTest {

    private static final String TEST_STATUS_1 = "New";
    private static final String TEST_STATUS_2 = "In process";
    private static final String TEST_UPDATED_STATUS = "Custom status";
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
    void testCreateStatus() throws Exception {
        ResultActions creationResult = createStatus(TEST_STATUS_1)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(TEST_STATUS_1)));

        Long statusId = extractStatusIdFromResponse(creationResult);

        assertTrue(statusRepository.existsById(statusId));
    }

    @Test
    void testFindAllStatuses() throws Exception {
        createStatus(TEST_STATUS_1)
            .andExpect(status().isCreated());

        createStatus(TEST_STATUS_2)
            .andExpect(status().isCreated());

        MockHttpServletResponse response = mvc.perform(get("/api/statuses"))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        List<Status> statuses = fromJson(response.getContentAsString(), new TypeReference<List<Status>>() { });
        List<Status> expected = statusRepository.findAll();

        assertEquals(statuses.size(), expected.size());
    }

    @Test
    void testFindStatusById() throws Exception {
        createStatus(TEST_STATUS_1)
            .andExpect(status().isCreated());

        Status existedStatus = statusRepository.findAll().get(0);
        long statusId = existedStatus.getId();

        MockHttpServletResponse response = mvc.perform(get("/api/statuses/%d".formatted(statusId)))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        StatusDTO statusDTO = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );

        assertEquals(existedStatus.getId(), statusDTO.getId());
        assertEquals(existedStatus.getName(), statusDTO.getName());
    }

    @Test
    void testUpdateStatus() throws Exception {
        createStatus(TEST_STATUS_1)
            .andExpect(status().isCreated());

        Status statusToUpdate = statusRepository.findAll().get(0);
        long statusId = statusToUpdate.getId();
        StatusDTO dto = new StatusDTO(TEST_UPDATED_STATUS);

        mvc.perform(put("/api/statuses/%d".formatted(statusId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                .content(testUtils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(statusId), Long.class))
            .andExpect(jsonPath("$.name", is(TEST_UPDATED_STATUS)));

        Optional<Status> actual = statusRepository.findTaskStatusById(statusId);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_STATUS, actual.get().getName());
    }

    @Test
    void testDeleteStatus() throws Exception {
        createStatus(TEST_STATUS_1)
            .andExpect(status().isCreated());

        Status statusToDelete = statusRepository.findAll().get(0);
        long statusId = statusToDelete.getId();

        mvc.perform(delete("/api/statuses/%d".formatted(statusId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
            .andExpect(status().isOk());

        assertFalse(statusRepository.existsById(statusId));
    }

    private ResultActions createStatus(String name) throws Exception {
        StatusDTO statusRequestDTO = new StatusDTO(name);

        return mvc.perform(post("/api/statuses")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
            .content(testUtils.toJson(statusRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }

    private Long extractStatusIdFromResponse(ResultActions resultActions) throws Exception {
        MvcResult result = resultActions.andReturn();
        String responceContent = result.getResponse().getContentAsString();
        TypeReference<Status> typeReference = new TypeReference<Status>() { };
        Status status = fromJson(responceContent, typeReference);
        return status.getId();

    }
}

package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.domain.dto.TaskRequestDTO;
import hexlet.code.domain.dto.TaskResponseDTO;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.Task;
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
class TaskControllerTest {

    private static final String TEST_TASK_NAME = "Тестовая задача";
    private static final String TEST_TASK_DESCRIPTION = "Тестовое описание";
    private static final String TEST_UPDATED_TASK_NAME = "Измененная задача";
    private static final String TEST_UPDATED_TASK_DESCRIPTION = "Измененное описание";
    private static final String TEST_EMAIL_1 = "email.1@testmail.com";
    private static final String TEST_EMAIL_2 = "email.2@testmail.com";
    private static final String TEST_STATUS_NAME_1 = "Новая";
    private static final String TEST_STATUS_NAME_2 = "В работе";

    private User testAuthor;
    private User testExecutor;
    private Status testStatusNew;
    private Status testStatusInProgress;
    private String authorToken;
    private String executorToken;

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
        testAuthor = userRepository.save(testUtils.buildDefaultUser(TEST_EMAIL_1));
        testExecutor = userRepository.save(testUtils.buildDefaultUser(TEST_EMAIL_2));
        testStatusNew = statusRepository.save(testUtils.buildDefaultStatus(TEST_STATUS_NAME_1));
        testStatusInProgress = statusRepository.save(testUtils.buildDefaultStatus(TEST_STATUS_NAME_2));
        authorToken = jwtUtils.generateToken(testAuthor);
        executorToken = jwtUtils.generateToken(testExecutor);
    }

    @AfterEach
    void afterEach() {
        taskRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAllTasks() throws Exception {
        createTask().andExpect(status().isCreated());
        createTask().andExpect(status().isCreated());

        MockHttpServletResponse response = mvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        List<TaskResponseDTO> taskDTOList = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );
        int expectedDtoCount = 2;
        int actual = taskDTOList.size();

        assertEquals(expectedDtoCount, actual);
        assertEquals(TEST_TASK_NAME, taskDTOList.get(0).getName());
        assertEquals(TEST_TASK_NAME, taskDTOList.get(1).getName());
    }

    @Test
    void findTaskById() throws Exception {
        createTask().andExpect(status().isCreated());

        Task existedTask = taskRepository.findAll().get(0);
        long taskId = existedTask.getId();

        MockHttpServletResponse response = mvc.perform(get("/api/tasks/%d".formatted(taskId)))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        TaskResponseDTO taskDTO = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );

        assertEquals(existedTask.getId(), taskDTO.getId());
        assertEquals(testAuthor.getId(), taskDTO.getAuthor().getId());
        assertEquals(testExecutor.getId(), taskDTO.getExecutor().getId());
        assertEquals(testStatusNew.getId(), taskDTO.getTaskStatus().getId());
        assertEquals(TEST_TASK_NAME, taskDTO.getName());
        assertEquals(TEST_TASK_DESCRIPTION, taskDTO.getDescription());
    }

    @Test
    void testCreateTask() throws Exception {
        long expectedCountInDB = 0;
        long actualCount = taskRepository.count();

        assertEquals(expectedCountInDB, actualCount);

        createTask()
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", is(TEST_TASK_NAME)))
            .andExpect(jsonPath("$.description", is(TEST_TASK_DESCRIPTION)));

        expectedCountInDB = 1;
        actualCount = taskRepository.count();

        assertEquals(expectedCountInDB, actualCount);
    }

    @Test
    void testUpdateTask() throws Exception {
        createTask().andExpect(status().isCreated());

        Task taskToUpdate = taskRepository.findAll().get(0);
        long taskId = taskToUpdate.getId();
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
            TEST_UPDATED_TASK_NAME,
            TEST_UPDATED_TASK_DESCRIPTION,
            testAuthor.getId(),
            testStatusInProgress.getId(),
            null
        );

        mvc.perform(put("/api/tasks/%d".formatted(taskId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken)
                .content(testUtils.toJson(taskRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(taskId), Long.class))
            .andExpect(jsonPath("$.name", is(TEST_UPDATED_TASK_NAME)))
            .andExpect(jsonPath("$.description", is(TEST_UPDATED_TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.executor.id", is(testAuthor.getId()), Long.class))
            .andExpect(jsonPath("$.taskStatus.id", is(testStatusInProgress.getId()), Long.class));

        Optional<Task> actual = taskRepository.findTaskById(taskId);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_TASK_NAME, actual.get().getName());
        assertEquals(TEST_UPDATED_TASK_DESCRIPTION, actual.get().getDescription());
        assertEquals(testAuthor.getId(), actual.get().getAuthor().getId());
        assertEquals(testStatusInProgress.getId(), actual.get().getTaskStatus().getId());
    }

    @Test
    void testDeleteTask() throws Exception {
        createTask().andExpect(status().isCreated());

        Task taskToDelete = taskRepository.findAll().get(0);
        long taskId = taskToDelete.getId();

        mvc.perform(delete("/api/tasks/%d".formatted(taskId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken))
            .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(taskId));
    }

    @Test
    void testUpdateOrDeleteTaskByNotTheOwner() throws Exception {
        createTask().andExpect(status().isCreated());

        Task taskToUpdateOrDelete = taskRepository.findAll().get(0);
        long taskId = taskToUpdateOrDelete.getId();
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
            TEST_UPDATED_TASK_NAME,
            TEST_UPDATED_TASK_DESCRIPTION,
            testAuthor.getId(),
            testStatusInProgress.getId(),
            null
        );

        mvc.perform(put("/api/tasks/%d".formatted(taskId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + executorToken)
                .content(testUtils.toJson(taskRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

        mvc.perform(delete("/api/tasks/%d".formatted(taskId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + executorToken))
            .andExpect(status().isForbidden());
    }

    private ResultActions createTask() throws Exception {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(
            TEST_TASK_NAME,
            TEST_TASK_DESCRIPTION,
            testExecutor.getId(),
            testStatusNew.getId(),
            null
        );

        return mvc.perform(post("/api/tasks")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken)
            .content(testUtils.toJson(taskRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }
}

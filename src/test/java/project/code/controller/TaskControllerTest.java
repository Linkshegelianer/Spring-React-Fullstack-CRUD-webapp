package project.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import project.code.dto.TaskDTO;
import project.code.domain.Status;
import project.code.domain.Task;
import project.code.domain.User;
import project.code.repository.TaskRepository;
import project.code.repository.StatusRepository;
import project.code.repository.UserRepository;
import project.code.security.JWTUtils;
import project.code.utils.TestUtils;
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

import static project.code.utils.TestUtils.TOKEN_PREFIX;
import static project.code.utils.TestUtils.fromJson;
import static java.nio.charset.StandardCharsets.UTF_8;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureMockMvc
class TaskControllerTest {

    private static final String TEST_TASK = "Test task";
    private static final String TEST_TASK_DESCRIPTION = "Test task description";
    private static final String TEST_UPDATED_TASK = "Updated task";
    private static final String TEST_UPDATED_TASK_DESCRIPTION = "Updated task description";
    private static final String TEST_EMAIL_1 = "test1@mail.com";
    private static final String TEST_EMAIL_2 = "test2@mail.com";
    private static final String TEST_STATUS_1 = "New";
    private static final String TEST_STATUS_2 = "In process";

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
        testStatusNew = statusRepository.save(testUtils.buildDefaultStatus(TEST_STATUS_1));
        testStatusInProgress = statusRepository.save(testUtils.buildDefaultStatus(TEST_STATUS_2));
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
    void testCreateTask() throws Exception {
        ResultActions creationResult = createTask()
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final Long taskId = extractTaskIdFromResponse(creationResult);
        final Optional<Task> actual = taskRepository.findTaskById(taskId);

        assertTrue(taskRepository.existsById(taskId));
        assertEquals(TEST_TASK, actual.map(Task::getName).orElse(null));
        assertEquals(TEST_TASK_DESCRIPTION, actual.map(Task::getDescription).orElse(null));
    }

    @Test
    void testFindAllTasks() throws Exception {
        createTask().andExpect(status().isCreated());
        createTask().andExpect(status().isCreated());

        final MockHttpServletResponse response = mvc.perform(get("/api/tasks"))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        final List<TaskDTO> taskDTOList = testUtils.fromJson(response.getContentAsString(), new TypeReference<>() { });

        for (TaskDTO taskDTO : taskDTOList) {
            assertEquals(TEST_TASK, taskDTO.getName());
        }
    }

    @Test
    void findTaskById() throws Exception {
        createTask().andExpect(status().isCreated());

        final Task existedTask = taskRepository.findAll().get(0);
        final long taskId = existedTask.getId();

        final MockHttpServletResponse response = mvc.perform(get("/api/tasks/%d".formatted(taskId)))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        final TaskDTO taskDTO = testUtils.fromJson(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );

        assertEquals(existedTask.getId(), taskDTO.getId());
        assertEquals(testAuthor.getId(), taskDTO.getAuthor().getId());
        assertEquals(testExecutor.getId(), taskDTO.getExecutor().getId());
        assertEquals(testStatusNew.getId(), taskDTO.getTaskStatus().getId());
        assertEquals(TEST_TASK, taskDTO.getName());
        assertEquals(TEST_TASK_DESCRIPTION, taskDTO.getDescription());
    }

    @Test
    void testUpdateTask() throws Exception {
        createTask().andExpect(status().isCreated());

        final Task taskToUpdate = taskRepository.findAll().get(0);
        final long taskId = taskToUpdate.getId();
        final TaskDTO taskRequestDTO = new TaskDTO(
                TEST_UPDATED_TASK,
                TEST_UPDATED_TASK_DESCRIPTION,
                testAuthor.getId(),
                testStatusInProgress.getId()
        );

        final MockHttpServletResponse response = mvc.perform(put("/api/tasks/%d".formatted(taskId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken)
                .content(testUtils.asJson(taskRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        final long id = fromJson(response.getContentAsString(), new TypeReference<Task>() { }).getId();
        final Optional<Task> actual = taskRepository.findTaskById(id);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_TASK, actual.map(Task::getName).orElse(null));
        assertEquals(TEST_UPDATED_TASK_DESCRIPTION, actual.map(Task::getDescription).orElse(null));
        assertEquals(testAuthor.getId(), actual.map(Task::getAuthor).map(User::getId).orElse(null));
        assertEquals(testStatusInProgress.getId(), actual.map(Task::getTaskStatus).map(Status::getId)
                .orElse(null));
    }

    @Test
    void testDeleteTask() throws Exception {
        createTask().andExpect(status().isCreated());

        final Task taskToDelete = taskRepository.findAll().get(0);
        final long taskId = taskToDelete.getId();

        mvc.perform(delete("/api/tasks/%d".formatted(taskId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken))
                .andExpect(status().isOk());

        assertFalse(taskRepository.existsById(taskId));
    }

    @Test
    void testUpdateOrDeleteTaskByNotTheOwner() throws Exception {
        createTask().andExpect(status().isCreated());

        final Task taskToUpdateOrDelete = taskRepository.findAll().get(0);
        final long taskId = taskToUpdateOrDelete.getId();
        final TaskDTO taskRequestDTO = new TaskDTO(
                TEST_UPDATED_TASK,
                TEST_UPDATED_TASK_DESCRIPTION,
                testAuthor.getId(),
                testStatusInProgress.getId()
        );

        mvc.perform(put("/api/tasks/%d".formatted(taskId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + executorToken)
                        .content(testUtils.asJson(taskRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mvc.perform(delete("/api/tasks/%d".formatted(taskId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + executorToken))
                .andExpect(status().isForbidden());
    }

    private ResultActions createTask() throws Exception {
        TaskDTO taskRequestDTO = new TaskDTO(
                TEST_TASK,
            TEST_TASK_DESCRIPTION,
            testExecutor.getId(),
            testStatusNew.getId()
        );

        return mvc.perform(post("/api/tasks")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + authorToken)
            .content(testUtils.asJson(taskRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }

    private Long extractTaskIdFromResponse(ResultActions resultActions) throws Exception {
        MvcResult result = resultActions.andReturn();
        String responseContent = result.getResponse().getContentAsString();
        TypeReference<Task> typeReference = new TypeReference<Task>() { };
        Task task = fromJson(responseContent, typeReference);
        return task.getId();
    }
}

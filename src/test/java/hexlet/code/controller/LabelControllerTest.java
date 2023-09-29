package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.dto.LabelDTO;
import hexlet.code.domain.Label;
import hexlet.code.domain.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JWTUtils;
import hexlet.code.utils.TestUtils;
import org.assertj.core.api.Assertions;
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
class LabelControllerTest {

    private static final String TEST_LABEL_1 = "Label 1";
    private static final String TEST_LABEL_2 = "Label 2";
    private static final String TEST_UPDATED_LABEL = "Updated label";
    private String token;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LabelRepository labelRepository;
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
        labelRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateLabel() throws Exception {
        ResultActions creationResult = createLabel(TEST_LABEL_1)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(TEST_LABEL_1)));

        Long labelId = extractLabelIdFromResponse(creationResult);

        assertTrue(labelRepository.existsById(labelId));
    }

    @Test
    void testFindAllLabels() throws Exception {
        createLabel(TEST_LABEL_1)
                .andExpect(status().isCreated());

        createLabel(TEST_LABEL_2)
                .andExpect(status().isCreated());

        MockHttpServletResponse response = mvc.perform(get("/api/labels")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<List<Label>>() { });
        List<Label> expected = labelRepository.findAll();

         Assertions.assertThat(labels).containsAll(expected);

    }

    @Test
    void testFindLabelById() throws Exception {
        createLabel(TEST_LABEL_1)
            .andExpect(status().isCreated());

        Label existedLabel = labelRepository.findAll().get(0);
        long labelId = existedLabel.getId();

        MockHttpServletResponse response = mvc.perform(get("/api/labels/%d".formatted(labelId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
            .andExpect(status().isOk())
            .andReturn().getResponse();

        LabelDTO labelDTO = testUtils.jsonToObject(
            response.getContentAsString(UTF_8),
            new TypeReference<>() { }
        );

        assertEquals(existedLabel.getId(), labelDTO.getId());
        assertEquals(existedLabel.getName(), labelDTO.getName());
    }

    @Test
    void testUpdateLabel() throws Exception {
        createLabel(TEST_LABEL_1)
            .andExpect(status().isCreated());

        Label labelToUpdate = labelRepository.findAll().get(0);
        long labelId = labelToUpdate.getId();
        LabelDTO dto = new LabelDTO(TEST_UPDATED_LABEL);

        mvc.perform(put("/api/labels/%d".formatted(labelId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                .content(testUtils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(labelId), Long.class))
            .andExpect(jsonPath("$.name", is(TEST_UPDATED_LABEL)));

        Optional<Label> actual = labelRepository.findLabelById(labelId);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_LABEL, actual.map(Label::getName).orElse(null));
    }

    @Test
    void testDeleteLabel() throws Exception {
        ResultActions creationResult = createLabel(TEST_LABEL_1);
        Long labelId = extractLabelIdFromResponse(creationResult);

        mvc.perform(delete("/api/labels/%d".formatted(labelId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        assertFalse(labelRepository.existsById(labelId));
    }

    private ResultActions createLabel(String name) throws Exception {
        LabelDTO labelRequestDTO = new LabelDTO(name);

        return mvc.perform(post("/api/labels")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
            .content(testUtils.toJson(labelRequestDTO))
            .contentType(MediaType.APPLICATION_JSON));
    }

    private Long extractLabelIdFromResponse(ResultActions resultActions) throws Exception {
        MvcResult result = resultActions.andReturn();
        String responseContent = result.getResponse().getContentAsString();
        TypeReference<Label> typeReference = new TypeReference<Label>() { };
        Label label = fromJson(responseContent, typeReference);
        return label.getId();
    }
}

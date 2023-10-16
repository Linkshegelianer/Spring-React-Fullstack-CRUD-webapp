package project.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import project.code.dto.LabelDTO;
import project.code.domain.Label;
import project.code.domain.User;
import project.code.repository.LabelRepository;
import project.code.repository.TaskRepository;
import project.code.repository.UserRepository;
import project.code.security.JWTUtils;
import project.code.utils.TestUtils;
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
        final ResultActions creationResult = createLabel(TEST_LABEL_1)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final Long labelId = extractLabelIdFromResponse(creationResult);
        final Optional<Label> actual = labelRepository.findLabelById(labelId);

        assertTrue(labelRepository.existsById(labelId));
        assertEquals(TEST_LABEL_1, actual.map(Label::getName).orElse(null));
    }

    @Test
    void testFindAllLabels() throws Exception {
        createLabel(TEST_LABEL_1).andExpect(status().isCreated());
        createLabel(TEST_LABEL_2).andExpect(status().isCreated());

        final MockHttpServletResponse response = mvc.perform(get("/api/labels")
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<List<Label>>() { });
        final List<Label> expected = labelRepository.findAll();

        Assertions.assertThat(labels).containsAll(expected);
    }

    @Test
    void testFindLabelById() throws Exception {
        createLabel(TEST_LABEL_1).andExpect(status().isCreated());

        final Label existedLabel = labelRepository.findAll().get(0);
        final long labelId = existedLabel.getId();

        final MockHttpServletResponse response = mvc.perform(get("/api/labels/%d".formatted(labelId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final LabelDTO labelDTO = testUtils.fromJson(response.getContentAsString(UTF_8), new TypeReference<>() { });

        assertEquals(existedLabel.getId(), labelDTO.getId());
        assertEquals(existedLabel.getName(), labelDTO.getName());
    }

    @Test
    void testUpdateLabel() throws Exception {
        createLabel(TEST_LABEL_1).andExpect(status().isCreated());

        final Label labelToUpdate = labelRepository.findAll().get(0);
        final long labelId = labelToUpdate.getId();
        final LabelDTO dto = new LabelDTO(TEST_UPDATED_LABEL);

        final MockHttpServletResponse response = mvc.perform(put("/api/labels/%d".formatted(labelId))
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
                .content(testUtils.asJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        final long id = fromJson(response.getContentAsString(), new TypeReference<Label>() { }).getId();
        final Optional<Label> actual = labelRepository.findLabelById(id);

        assertNotNull(actual.orElse(null));
        assertEquals(TEST_UPDATED_LABEL, actual.map(Label::getName).orElse(null));
    }

    @Test
    void testDeleteLabel() throws Exception {
        final ResultActions creationResult = createLabel(TEST_LABEL_1);
        final Long labelId = extractLabelIdFromResponse(creationResult);

        mvc.perform(delete("/api/labels/%d".formatted(labelId))
                        .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token))
                .andExpect(status().isOk());

        assertFalse(labelRepository.existsById(labelId));
    }

    private ResultActions createLabel(String name) throws Exception {
        final LabelDTO labelRequestDTO = new LabelDTO(name);

        return mvc.perform(post("/api/labels")
            .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + token)
            .content(testUtils.asJson(labelRequestDTO))
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

package br.com.dio.tasklist.infrastructure.http;

import br.com.dio.tasklist.application.CreateTaskUseCase;
import br.com.dio.tasklist.application.GetTaskByIdUseCase;
import br.com.dio.tasklist.application.GetTasksUseCase;
import br.com.dio.tasklist.application.UpdateTaskUseCase;
import br.com.dio.tasklist.application.input.CreateTaskInput;
import br.com.dio.tasklist.application.input.UpdateTaskInput;
import br.com.dio.tasklist.application.output.DeteleTaskUseCase;
import br.com.dio.tasklist.application.output.TaskOutput;
import br.com.dio.tasklist.domain.TaskId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
class TaskControllerTest {

    @Mock
    CreateTaskUseCase createTaskUseCase;
    @Mock
    GetTasksUseCase getTasksUseCase;
    @Mock
    GetTaskByIdUseCase getTaskByIdUseCase;
    @Mock
    DeteleTaskUseCase deleteTaskUseCase;
    @Mock
    UpdateTaskUseCase updateTaskUseCase;

    @InjectMocks
    TaskController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void createsTaskAndDocumentsRequestAndResponse() throws Exception {
        var output = new TaskOutput("task-id", "Buy groceries", Optional.of("Milk and bread"), "PENDING");
        when(createTaskUseCase.execute(new CreateTaskInput("Buy groceries", Optional.of("Milk and bread"))))
                .thenReturn(output);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Buy groceries",
                                  "description": "Milk and bread"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "task-id",
                          "title": "Buy groceries",
                          "description": "Milk and bread",
                          "status": "PENDING"
                        }
                        """))
                .andDo(document("tasks/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("Task title"),
                                fieldWithPath("description").description("Optional task description")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Task identifier"),
                                fieldWithPath("title").description("Task title"),
                                fieldWithPath("description").description("Task description"),
                                fieldWithPath("status").description("Current task status")
                        )));
    }

    @Test
    void listsTasksAndDocumentsResponse() throws Exception {
        when(getTasksUseCase.execute()).thenReturn(List.of(
                new TaskOutput("task-id", "Buy groceries", Optional.of("Milk and bread"), "PENDING")
        ));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{
                          "id": "task-id",
                          "title": "Buy groceries",
                          "description": "Milk and bread",
                          "status": "PENDING"
                        }]
                        """))
                .andDo(document("tasks/list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("Task identifier"),
                                fieldWithPath("[].title").description("Task title"),
                                fieldWithPath("[].description").description("Task description"),
                                fieldWithPath("[].status").description("Current task status")
                        )));

        verify(getTasksUseCase).execute();
    }

    @Test
    void readsTaskAndDocumentsPathAndResponse() throws Exception {
        var id = UUID.randomUUID();
        when(getTaskByIdUseCase.execute(new TaskId(id)))
                .thenReturn(new TaskOutput(id.toString(), "Buy groceries", Optional.of("Milk and bread"), "PENDING"));

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "%s",
                          "title": "Buy groceries",
                          "description": "Milk and bread",
                          "status": "PENDING"
                        }
                        """.formatted(id)))
                .andDo(document("tasks/read",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("Task identifier")),
                        responseFields(
                                fieldWithPath("id").description("Task identifier"),
                                fieldWithPath("title").description("Task title"),
                                fieldWithPath("description").description("Task description"),
                                fieldWithPath("status").description("Current task status")
                        )));

        verify(getTaskByIdUseCase).execute(new TaskId(id));
    }

    @Test
    void deletesTaskAndDocumentsPath() throws Exception {
        var id = UUID.randomUUID();

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(document("tasks/delete",
                        pathParameters(parameterWithName("id").description("Task identifier"))));

        verify(deleteTaskUseCase).execute(new TaskId(id));
    }

    @Test
    void updatesTaskAndDocumentsRequestAndResponse() throws Exception {
        var id = UUID.randomUUID();
        var input = new UpdateTaskInput(
                Optional.of("Buy food"),
                Optional.of("Milk and bread"),
                Optional.of("COMPLETED")
        );
        when(updateTaskUseCase.execute(new TaskId(id), input))
                .thenReturn(new TaskOutput(id.toString(), "Buy food", Optional.of("Milk and bread"), "COMPLETED"));

        mockMvc.perform(patch("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Buy food",
                                  "description": "Milk and bread",
                                  "status": "COMPLETED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "%s",
                          "title": "Buy food",
                          "description": "Milk and bread",
                          "status": "COMPLETED"
                        }
                        """.formatted(id)))
                .andDo(document("tasks/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("Task identifier")),
                        requestFields(
                                fieldWithPath("title").description("Updated task title"),
                                fieldWithPath("description").description("Updated task description"),
                                fieldWithPath("status").description("Updated task status")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Task identifier"),
                                fieldWithPath("title").description("Task title"),
                                fieldWithPath("description").description("Task description"),
                                fieldWithPath("status").description("Current task status")
                        )));

        verify(updateTaskUseCase).execute(new TaskId(id), input);
    }
}

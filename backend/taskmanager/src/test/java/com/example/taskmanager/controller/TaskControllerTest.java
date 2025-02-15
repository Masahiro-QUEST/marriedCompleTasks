package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {TaskController.class})
@ComponentScan(
    basePackages = "com.example.taskmanager.controller",
    useDefaultFilters = false,
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TaskController.class)
)
@ImportAutoConfiguration({ JacksonAutoConfiguration.class, WebMvcAutoConfiguration.class })
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@WithMockUser  // セキュリティが有効な場合、モックユーザーを設定
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    // 全タスク取得のテスト
    @Test
    public void testGetTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tasks)));
    }
}

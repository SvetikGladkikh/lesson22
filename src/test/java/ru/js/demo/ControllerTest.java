package ru.js.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.js.demo.model.Message;
import ru.js.demo.service.MessageService;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    public void saveMessage() throws Exception {
        Message message = new Message();
        message.setText("hi");
        given(messageService.saveMessage(message)).willReturn("317a3143-d147-4c80-8dc4-710937a2d318");
        mockMvc.perform(MockMvcRequestBuilders.post("/message").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
        .andExpect(content().string("317a3143-d147-4c80-8dc4-710937a2d318"));
    }

    @Test
    public void saveMessageError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/message").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getMessageGoodRequest() throws Exception {
        Message message = new Message();
        message.setText("hi");
        given(messageService.getMessage("317a3143-d147-4c80-8dc4-710937a2d318")).willReturn(message);
        mockMvc.perform(get("/message/317a3143-d147-4c80-8dc4-710937a2d318")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.text", is("hi")));
    }

    @Test
    public void getSettings() {
        try {
            mockMvc.perform(get("/settings")).andExpect(status().isOk()).andExpect(jsonPath("$", is("value1")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
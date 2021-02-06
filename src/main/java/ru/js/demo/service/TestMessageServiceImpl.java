package ru.js.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.js.demo.model.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Profile("test")
public class TestMessageServiceImpl implements MessageService {
    private Logger logger = LoggerFactory.getLogger(TestMessageServiceImpl.class);
    final private String FILE_PATH = "messageStorage.json";

    @Override
    public String saveMessage(Message message) throws IOException {
        if (message == null)
            throw new RuntimeException("Message is null.");

        String id = UUID.randomUUID().toString();
        Map<String, Message> messageStore = readJSON();

        messageStore.put(id, message);
        writeJSON(messageStore);

        logger.info("Message: " + message.getText() + ", id: " + id);

        return id;
    }

    @Override
    public Message getMessage(String id) throws IOException {
        if (id == null)
            throw new RuntimeException("id is null.");

        Map<String, Message> messageStore = readJSON();
        return messageStore.get(id);
    }

    private Map<String, Message> readJSON() throws IOException {
        prepareFile();
        // чтение json-файла в память
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Message>> typeRef = new TypeReference<HashMap<String, Message>>() {};
        return mapper.readValue(new File(FILE_PATH), typeRef);
    }

    private void writeJSON(Map<String, Message> messageStore) throws IOException {
        prepareFile();
        // сериализация с помощью встроенного механизма сериализации
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(FILE_PATH), messageStore);
    }

    private void prepareFile() throws IOException {
        if(!Files.exists(Paths.get(FILE_PATH), new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            try(FileOutputStream outputStream = new FileOutputStream(FILE_PATH)){
                outputStream.write("{}".getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}

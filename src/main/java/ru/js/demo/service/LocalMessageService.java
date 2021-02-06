package ru.js.demo.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.js.demo.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Profile("local")
public class LocalMessageService implements MessageService {
    private Map<String, Message> messageStore = new HashMap<>();

    @Override
    public String saveMessage(Message message) {
        if (message == null)
            throw new RuntimeException("Message is null.");

        String id = UUID.randomUUID().toString();

        messageStore.put(id, message);
        return id;
    }

    @Override
    public Message getMessage(String id) {
        if (id == null)
            throw new RuntimeException("id is null.");

        return messageStore.get(id);
    }
}

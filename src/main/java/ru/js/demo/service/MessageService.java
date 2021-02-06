package ru.js.demo.service;

import ru.js.demo.model.Message;

import java.io.IOException;

public interface MessageService {
    String saveMessage(Message message) throws IOException;
    Message getMessage(String id) throws IOException;
}

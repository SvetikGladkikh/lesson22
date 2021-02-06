package ru.js.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.js.demo.model.Message;
import ru.js.demo.service.MessageService;

import java.io.IOException;

@RestController
public class Controller {
    private MessageService messageService;
    private Environment env;

    @Autowired
    public Controller(MessageService messageService, Environment env) {
        this.messageService = messageService;
        this.env = env;
    }

    @PostMapping(path= "/message", consumes = "application/json", produces = "application/json")
    public String saveMessage(@RequestBody Message message) {
        String text = message.getText();

        if (text == null || "".equals(text)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }

        try {
            return messageService.saveMessage(message);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/message/{id}", produces = "application/json")
    public Message getMessage(@PathVariable String id) {
        if (id == null || "".equals(id)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST);
        }
        try {
            return messageService.getMessage(id);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path= "/settings", produces = "application/json")
    public String getSettings() {
        return env.getProperty("test.my.value");
    }
}

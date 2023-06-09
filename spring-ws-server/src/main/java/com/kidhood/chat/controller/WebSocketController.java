package com.kidhood.chat.controller;

import com.kidhood.chat.kafka.KafkaMessageProducer;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private UserService userService;

    @MessageMapping("/subscribe/{username}/{channel}")
    public void subscribeToChannel (@DestinationVariable String username, @DestinationVariable String channel, Message message) {
        kafkaMessageProducer.sendMessage(username,channel,message);
    }

//    @MessageMapping("/users-list")
//    @SendTo("/topic/users")
//    public ResponseEntity<?> getUsers() {
//        // Retrieve users from the database
//        return userService.getListUser();
//    }
}

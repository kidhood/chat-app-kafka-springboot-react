package com.kidhood.chat.controller;

import com.kidhood.chat.constant.KafkaConstant;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    private final MessageService messageService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@DestinationVariable("user") String user, @Payload Message message){
//        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        log.info("{}", message.toString());
        return message;
    }

    @PostMapping(value = "/api/send/private", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendMessagePrivate(@RequestBody Message message){
        message.setDate(LocalDateTime.now().toString());
        ListenableFuture<SendResult<String, Message>> future =
                kafkaTemplate.send(KafkaConstant.KAFKA_CHANNEL_USER, message);
        try {
            SendResult<String, Message> response = future.get();
            System.out.println("Record metadata : "+response.getRecordMetadata());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/send/public", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendMessagePublic(@RequestBody Message message){
        message.setDate(LocalDateTime.now().toString());
        ListenableFuture<SendResult<String, Message>> future =
                kafkaTemplate.send(KafkaConstant.KAFKA_CHANNEL_PUBLIC, message);
        try {
            SendResult<String, Message> response = future.get();
            System.out.println("Record metadata : "+response.getRecordMetadata());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/messages/{username}/{sender}")
    public ResponseEntity<?> getHistoryMessage(@PathVariable String username, @PathVariable String sender) {
        return messageService.getHistoryChat(username, sender);
    }
}

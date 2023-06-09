package com.kidhood.chat.listener;

import com.kidhood.chat.entity.MessageEntity;
import com.kidhood.chat.entity.UserEntity;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.repository.ChanelRepository;
import com.kidhood.chat.repository.UserRepository;
import com.kidhood.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChanelRepository chanelRepository;

    //It will listen for the Kafka queue messages.
//    @KafkaListener(
//            topics = KafkaConstant.KAFKA_TOPIC,
//            groupId = KafkaConstant.CONSUMER_GROUP_ID
//    )
    public void listen(Message message) {
        System.out.println("Sending message via kafka listener.."+message);
        //It will convert the message and send that to WebSocket topic.

        MessageEntity mess = new MessageEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(message.getSenderName());
        var tempUser = userRepository.findByUserName(message.getSenderName());
        if(!tempUser.isPresent()){
            userRepository.save(userEntity);
            mess.setSender(userEntity);
        }else {
            mess.setSender(tempUser.get());
        }
        mess.setContent(message.getMessage());
        messageService.saveMessage(mess);
        template.convertAndSend("/topic/group", message);
    }


//    @KafkaListener(
//            topics = KafkaConstant.KAFKA_TOPIC,
//            groupId = KafkaConstant.CONSUMER_GROUP_ID
//    )
//    public void consumeMessages(ConsumerRecord<String, Message> record) {
//        String key = record.key();
//        Message value = record.value();
//
//        // Process the message based on the key
//        if ("HI".equals(key)) {
//            // Perform desired logic with the message value
//            System.out.println("Received message with key: " + key + ", value: " + value);
//        }
//    }

}

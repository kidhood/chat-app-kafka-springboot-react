package com.kidhood.chat.kafka;

import com.kidhood.chat.constant.KafkaConstant;
import com.kidhood.chat.constant.MessageConstant;
import com.kidhood.chat.entity.ChanelEntity;
import com.kidhood.chat.entity.MessageEntity;
import com.kidhood.chat.entity.UserEntity;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.service.ChannelService;
import com.kidhood.chat.service.MessageService;
import com.kidhood.chat.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class KafkaMessageConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @KafkaListener(topics = KafkaConstant.KAFKA_CHANNEL_PUBLIC, groupId = KafkaConstant.CONSUMER_GROUP_ID)
    public void consumeMessagePublic(Message message) {

        // Construct the destination path based on user and channel
        String destination = String.format("/chatroom/public");
        log.info(String.format(MessageConstant.MESSAGE_SEND,message.getReceiverName(), "here", message.toString()));
        // Send the message payload to the WebSocket clients subscribed to the destination
        messagingTemplate.convertAndSend(destination,message);
//        messagingTemplate.convertAndSend(destination, message);
    }

    @KafkaListener(topics = KafkaConstant.KAFKA_CHANNEL_USER, groupId = KafkaConstant.CONSUMER_GROUP_ID)
    public void consumeMessagePrivate(Message message) {
        log.info("message {} ");
        //createUser or get
        UserEntity sender = userService.getUser(message.getSenderName());
        UserEntity reciever = userService.getUser(message.getReceiverName());
        //get channel
        ChanelEntity chanelEntity = channelService.getChanelEntity(Arrays.asList(sender,reciever));
        log.info("get ra dc chanel ne {}", chanelEntity.getId());
        //save message
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setChanel(chanelEntity);
        messageEntity.setSender(sender);
        messageEntity.setContent(message.getMessage());
        try{
//            messageEntity.setTimestamp(Date.valueOf(message.getDate()));
        }catch (Exception e){
//            log.info("khong cast ve lai duoc ne");
        }
        messageService.saveMessage(messageEntity);
        //
        String destination = String.format("/chatroom/%s/private", message.getReceiverName());
        log.info(String.format(MessageConstant.MESSAGE_SEND,message.getReceiverName(), "here", message.toString()));
        // Send the message payload to the WebSocket clients subscribed to the destination
        messagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
    }
}

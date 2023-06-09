package com.kidhood.chat.service;

import com.kidhood.chat.entity.MessageEntity;
import com.kidhood.chat.entity.UserEntity;
import com.kidhood.chat.mapper.MessageMapper;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.repository.ChanelRepository;
import com.kidhood.chat.repository.MessageRepository;
import com.kidhood.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;

    private final ChanelRepository chanelRepository;

    private final UserRepository userRepository;

    private final MessageMapper messageMapper;

    private static int MESSAGE_SIZE = 10;

    public boolean saveMessage(MessageEntity message) {
        try{
            messageRepository.save(message);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public ResponseEntity<?> getHistoryChat(String userName, String senderName){
        try{
            UserEntity sender = userRepository.findByUserName(senderName).get();
            UserEntity reciever = userRepository.findByUserName(userName).get();
            log.info("username {} sender {}", reciever.getUserName(),sender.getUserName());
            //get channel
            var chanelEntity = chanelRepository.findChannelByParticipants(sender.getId(), reciever.getId());
            log.info("User name {}", userName);
            PageRequest page = PageRequest.of(0,MESSAGE_SIZE,
                    Sort.by(Sort.Direction.ASC,"timestamp"));
            if(chanelEntity.isPresent()){
                var listMessage =  messageRepository.findByChanelId(chanelEntity.get().getId());
                if(listMessage.isPresent()){
                    List<Message> listMess = listMessage.get().stream().map(mess -> messageMapper.modelToDto(mess)).toList();
                    return ResponseEntity.ok(listMess);
                }
            }
       }catch (Exception e){
           log.info("dell co kenh chat do");

       }

        return null;
    }

}

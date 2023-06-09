package com.kidhood.chat.mapper;

import com.kidhood.chat.entity.MessageEntity;
import com.kidhood.chat.model.Message;
import com.kidhood.chat.model.Status;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(target = "message", source = "messageEntity.content")
    @Mapping(target = "senderName", source = "messageEntity.sender.userName")
    @Mapping(target = "date", expression = "java(messageEntity.getTimestamp().toString())")
    @Mapping(target = "status", expression = "java(defaultStatus())")
    @InheritInverseConfiguration
    Message modelToDto (MessageEntity messageEntity);

    MessageEntity dtoToModel(Message message);
    default Status defaultStatus() {
        return Status.MESSAGE; // Return the default enum value
    }
}

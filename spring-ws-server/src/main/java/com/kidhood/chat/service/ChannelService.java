package com.kidhood.chat.service;

import com.kidhood.chat.entity.ChanelEntity;
import com.kidhood.chat.entity.UserEntity;
import com.kidhood.chat.repository.ChanelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final ChanelRepository chanelRepository;

    public ChanelEntity getChanelEntity(List<UserEntity> userEntityList) {
        try {
            var channelTemp = chanelRepository.findChannelByParticipants(userEntityList.get(0).getId(), userEntityList.get(1).getId());
            return channelTemp.get();
        }catch (Exception e){
          ChanelEntity chanelEntity = new ChanelEntity();
          chanelEntity.setParticipants(userEntityList);
          chanelEntity.setChanelName(UUID.randomUUID().toString());
          chanelRepository.save(chanelEntity);
          return chanelEntity;
        }
    }
}

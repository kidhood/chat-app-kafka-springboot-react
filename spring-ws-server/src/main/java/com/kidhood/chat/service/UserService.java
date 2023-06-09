package com.kidhood.chat.service;

import com.kidhood.chat.entity.UserEntity;
import com.kidhood.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<?> getListUser() {
        var listUser = userRepository.findAll();
        log.info("Size {}", listUser.size());
        if(listUser != null)
            return ResponseEntity.ok(listUser);
        return null;
    }

    public List<UserEntity> getListUserObject() {
        var listUser = userRepository.findAll();
        log.info("Size {}", listUser.size());
        if(listUser != null)
            return listUser;
        return null;
    }

    public UserEntity getUser(String userName){
        var user = userRepository.findByUserName(userName);
        if(user.isPresent()){
            return user.get();
        }else{
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName(userName);
            userRepository.save(userEntity);
            return userEntity;
        }
    }
}

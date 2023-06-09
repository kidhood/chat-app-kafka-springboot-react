package com.kidhood.chat.repository;

import com.kidhood.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
//    List<MessageEntity> findAllByUserName(String userName, Pageable pageable);
    Optional<List<MessageEntity>> findByChanelId(long id);
}

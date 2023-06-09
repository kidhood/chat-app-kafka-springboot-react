package com.kidhood.chat.repository;

import com.kidhood.chat.entity.ChanelEntity;
import com.kidhood.chat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChanelRepository extends JpaRepository<ChanelEntity,Long> {
    @Query(nativeQuery = true, value = "SELECT tbl_chanel.id, tbl_chanel.chanel_name, tbl_chanel.date FROM tbl_chanel   \n" +
            "\tJOIN channel_participant AS B  ON tbl_chanel.id = B.channel_id\n" +
            "\t\tJOIN channel_participant AS C ON tbl_chanel.id = C.channel_id \n" +
            "\t\t\tWHERE B.user_id = ?1 AND C.user_id =?2")
    Optional<ChanelEntity> findChannelByParticipants(long id1, long id2);

    Optional<ChanelEntity> findByParticipantsIn(List<UserEntity> userEntityList);
}

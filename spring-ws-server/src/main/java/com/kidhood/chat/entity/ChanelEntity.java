package com.kidhood.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "tblChanel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChanelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String chanelName;
    @CreationTimestamp
    private Date date;

    @ManyToMany
    @JoinTable(
            name = "channel_participant",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants;

}

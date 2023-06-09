package com.kidhood.chat.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "tblMessage")
@Setter
@Getter
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "chanel_id")
    private ChanelEntity chanel;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    public MessageEntity() {
    }

    public MessageEntity(String content) {
        this.content = content;
    }

    public MessageEntity(Long id, UserEntity sender, String content, Date timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

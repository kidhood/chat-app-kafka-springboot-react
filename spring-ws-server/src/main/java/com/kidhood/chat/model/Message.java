package com.kidhood.chat.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;

    @Override
    public String toString() {
        return "Message{" +
                "senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                '}';
    }
}

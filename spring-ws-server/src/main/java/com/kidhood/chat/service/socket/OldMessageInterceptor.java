package com.kidhood.chat.service.socket;

import org.springframework.messaging.support.ChannelInterceptor;

//@Component
public class OldMessageInterceptor implements ChannelInterceptor {
//    @Autowired
//    private MessageService messageService;
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
////        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
////        if ("/chatroom".equals(accessor.getDestination())) {
////            String senderName = accessor.getUser().getName(); // Assuming the senderName is available in the user's name attribute
//
//            // Load old messages from the database based on the senderName
////            List<Message> oldMessages = messageService.getHistoryChat(senderName);
//
//            // Add the old messages to the message headers for further processing
////            accessor.setHeader("oldMessages", oldMessages);
//        }
//        return message;
//    }
}

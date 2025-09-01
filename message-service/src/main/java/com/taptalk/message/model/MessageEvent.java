package com.taptalk.message.model;

import lombok.Value;
import java.time.LocalDateTime;

@Value
public class MessageEvent {
    String messageId;
    String senderId;
    String receiverId;
    String receiverFcmToken;
    String content;
    LocalDateTime timestamp;
    boolean isRead;
    String chatRoomId;
} 
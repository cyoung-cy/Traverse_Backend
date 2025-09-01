package com.taptalk.notification.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String receiverFcmToken;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    private String chatRoomId;
} 
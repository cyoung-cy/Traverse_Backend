package com.taptalk.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.taptalk.notification.model.MessageEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final FirebaseMessaging firebaseMessaging;
    
    @KafkaListener(topics = "notifications", groupId = "notification-service")
    public void handleNotification(String message) {
        try {
            // 메시지 파싱 (실제 구현에서는 JSON 파싱 필요)
            String[] parts = message.split(":");
            String userId = parts[0];
            String title = parts[1];
            String body = parts[2];
            
            log.info("Received legacy notification for user: {} - {}: {}", userId, title, body);
            log.warn("Legacy notification format - consider migrating to MessageEvent format");
            
        } catch (Exception e) {
            log.error("Failed to process legacy notification", e);
        }
    }
    
    @KafkaListener(topics = "chat.unread", groupId = "notification-service", 
                   containerFactory = "messageEventKafkaListenerContainerFactory")
    public void handleUnreadMessage(MessageEvent messageEvent) {
        try {
            log.info("Received unread message event: {}", messageEvent);
            
            // FCM 토큰을 MessageEvent에서 직접 가져옴
            String fcmToken = messageEvent.getReceiverFcmToken();
            
            if (fcmToken == null || fcmToken.isEmpty() || "null".equals(fcmToken)) {
                log.warn("FCM token is empty for user: {}. Logging message for testing.", 
                        messageEvent.getReceiverId());
                
                // 테스트 환경에서는 알림을 보내지 않고 로그만 출력
                log.info("🔔 [TEST MODE] Would send notification to user '{}': '{}'", 
                        messageEvent.getReceiverId(), 
                        messageEvent.getContent().length() > 50 ? 
                            messageEvent.getContent().substring(0, 47) + "..." : 
                            messageEvent.getContent());
                
                log.info("📱 [TEST MODE] Notification data - messageId: {}, senderId: {}, chatRoomId: {}", 
                        messageEvent.getMessageId(), messageEvent.getSenderId(), messageEvent.getChatRoomId());
                
                return;
            }
            
            // FCM 메시지 생성
            String title = "새 메시지";
            String body = messageEvent.getContent();
            
            // 메시지가 너무 길면 줄임
            if (body.length() > 50) {
                body = body.substring(0, 47) + "...";
            }
            
            Message fcmMessage = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .putData("messageId", messageEvent.getMessageId())
                .putData("senderId", messageEvent.getSenderId())
                .putData("chatRoomId", messageEvent.getChatRoomId())
                .putData("timestamp", messageEvent.getTimestamp().toString())
                .build();
            
            // FCM 메시지 전송
            String response = firebaseMessaging.send(fcmMessage);
            log.info("🚀 Successfully sent FCM notification to user {}: {}", 
                    messageEvent.getReceiverId(), response);
            
        } catch (Exception e) {
            log.error("Failed to send unread message notification for user: {}", 
                     messageEvent.getReceiverId(), e);
        }
    }
} 
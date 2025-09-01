package com.taptalk.message.service;

import com.taptalk.message.model.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
    private static final String TOPIC = "chat.unread";

    public void handleNewMessage(MessageEvent message) {
        log.info("Processing message: messageId={}, senderId={}, receiverId={}, isRead={}", 
                message.getMessageId(), message.getSenderId(), message.getReceiverId(), message.isRead());
        
        try {
            // 1초 대기 (실제 환경에서는 데이터베이스 저장 등의 작업이 들어갈 수 있음)
            Thread.sleep(1000);
            
            // 메시지 읽음 상태 확인
            if (!message.isRead()) {
                // Kafka에 이벤트 발행 (비동기)
                CompletableFuture<SendResult<String, MessageEvent>> future = 
                    kafkaTemplate.send(TOPIC, message.getReceiverId(), message);
                
                future.whenComplete((result, throwable) -> {
                    if (throwable == null) {
                        log.info("Successfully published unread message event to Kafka: messageId={}, receiverId={}, partition={}, offset={}", 
                                message.getMessageId(), message.getReceiverId(), 
                                result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to publish unread message event to Kafka: messageId={}, receiverId={}", 
                                message.getMessageId(), message.getReceiverId(), throwable);
                    }
                });
                
                log.info("Unread message event queued for publication: messageId={}, receiverId={}", 
                        message.getMessageId(), message.getReceiverId());
            } else {
                log.info("Message already read, skipping notification: messageId={}", message.getMessageId());
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Message processing interrupted: messageId={}", message.getMessageId(), e);
            throw new RuntimeException("Message processing interrupted", e);
        } catch (Exception e) {
            log.error("Unexpected error while handling message: messageId={}", message.getMessageId(), e);
            throw new RuntimeException("Failed to handle message", e);
        }
    }
} 
package com.taptalk.message.controller;

import com.taptalk.message.model.MessageEvent;
import com.taptalk.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/check")
    public ResponseEntity<Void> checkMessageStatus(@RequestBody MessageEvent message) {
        log.info("Received message check request: messageId={}, senderId={}, receiverId={}", 
                message.getMessageId(), message.getSenderId(), message.getReceiverId());
        
        try {
            messageService.handleNewMessage(message);
            log.info("Message check completed successfully: messageId={}", message.getMessageId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to process message check: messageId={}", message.getMessageId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 
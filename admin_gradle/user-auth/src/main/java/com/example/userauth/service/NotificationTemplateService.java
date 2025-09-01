package com.example.userauth.service;

import com.example.userauth.dto.*;
import com.example.userauth.model.NotificationTemplate;
import com.example.userauth.repository.NotificationTemplateRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final NotificationTemplateRepository repository;

    private final Firestore firestore;

    public String createTemplate(NotificationTemplateRequest request) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        Timestamp now = Timestamp.now();

        NotificationTemplate template = new NotificationTemplate();
        template.setId(id);
        template.setName(request.getName());
        template.setType(request.getType());
        template.setContent(request.getContent());
        template.setVariables(request.getVariables());
        template.setCreated_at(now);
        template.setUpdated_at(now);

        ApiFuture<WriteResult> writeResult = firestore.collection("notification_templates")
                .document(id)
                .set(template);

        writeResult.get(); // 완료 보장
        return id;
    }

    public String updateTemplate(NotificationTemplateRequest request) throws ExecutionException, InterruptedException {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new IllegalArgumentException("ID가 필요합니다.");
        }

        DocumentReference docRef = firestore.collection("notification_templates").document(request.getId());
        DocumentSnapshot snapshot = docRef.get().get();

        if (!snapshot.exists()) {
            throw new IllegalArgumentException("해당 ID의 템플릿이 존재하지 않습니다.");
        }

        Map<String, Object> updates = Map.of(
                "name", request.getName(),
                "type", request.getType(),
                "content", request.getContent(),
                "variables", request.getVariables(),
                "updated_at", Timestamp.now()
        );

        ApiFuture<WriteResult> future = docRef.update(updates);
        future.get();

        return request.getId();
    }

    public Map<String, Object> getTemplates(int page, int limit, String type) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = repository.findTemplates(type);

        int total = documents.size();
        int totalPages = (int) Math.ceil((double) total / limit);
        int fromIndex = Math.min((page - 1) * limit, total);
        int toIndex = Math.min(fromIndex + limit, total);

        List<NotificationTemplateResponseDTO> templates = documents.subList(fromIndex, toIndex).stream().map(doc -> {
            NotificationTemplateResponseDTO dto = new NotificationTemplateResponseDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getString("name"));
            dto.setType(doc.getString("type"));
            dto.setContent(doc.getString("content"));
            dto.setCreated_at(doc.getTimestamp("created_at").toString());
            dto.setUpdated_at(doc.getTimestamp("updated_at").toString());
            dto.setUsage_count(doc.getLong("usage_count"));
            return dto;
        }).collect(Collectors.toList());

        Map<String, Object> data = new HashMap<>();
        data.put("templates", templates);
        data.put("total_count", total);
        data.put("current_page", page);
        data.put("total_pages", totalPages);

        return Map.of("success", true, "data", data);
    }

    public Map<String, Object> sendNotification(SendNotificationRequest request) throws Exception {
        // 1. 템플릿 가져오기
        DocumentSnapshot templateSnapshot = firestore.collection("notification_templates")
                .document(request.getTemplate_id())
                .get().get();

        if (!templateSnapshot.exists()) {
            throw new IllegalArgumentException("템플릿을 찾을 수 없습니다.");
        }

        Map<String, Object> template = templateSnapshot.getData();
        String contentTemplate = (String) template.get("content");
        String templateName = (String) template.get("name");
        String type = (String) template.get("type");

        // 2. 변수 적용
        String content = contentTemplate;
        if (request.getVariables() != null) {
            for (Map.Entry<String, String> entry : request.getVariables().entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        // 3. 알림 객체 생성 및 저장
        DocumentReference docRef = firestore.collection("notifications").document();
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("content", content);
        notificationData.put("template_id", request.getTemplate_id());
        notificationData.put("template_name", templateName);
        notificationData.put("type", type);
        notificationData.put("is_announcement", false);
        notificationData.put("recipient_count", request.getRecipients().size());
        notificationData.put("read_count", 0);
        notificationData.put("sent_by", "admin_web"); // 필요 시 실제 관리자 ID 설정
        notificationData.put("sent_at", Timestamp.now());
        notificationData.put("reference_id", UUID.randomUUID().toString());

        firestore.runTransaction(transaction -> {
            transaction.set(docRef, notificationData);
            return null;
        }).get();

        // 4. 사용자별 전송 처리 (간단히 recipient_id와 notification_id 저장)
        for (String userId : request.getRecipients()) {
            firestore.collection("user_notifications")
                    .add(Map.of(
                            "user_id", userId,
                            "notification_id", docRef.getId(),
                            "read", false,
                            "sent_at", Timestamp.now()
                    ));
        }

        return Map.of(
                "success", true,
                "message", "알림이 발송되었습니다",
                "data", Map.of(
                        "notification_id", docRef.getId(),
                        "sent_count", request.getRecipients().size()
                )
        );
    }
}
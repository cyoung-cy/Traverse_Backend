package com.taptalk.aimap.controller;

import com.taptalk.aimap.dto.quest.QuestResponse;
import com.taptalk.aimap.dto.quest.QuestSearchRequest;
import com.taptalk.aimap.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {
    private final QuestService questService;

    @PostMapping("/search")
    public ResponseEntity<QuestResponse> searchQuests(@RequestBody QuestSearchRequest request) {
        return ResponseEntity.ok(questService.searchQuests(request));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<QuestResponse> getRecommendations(@RequestBody QuestSearchRequest request) {
        return ResponseEntity.ok(questService.getRecommendations(request));
    }
} 
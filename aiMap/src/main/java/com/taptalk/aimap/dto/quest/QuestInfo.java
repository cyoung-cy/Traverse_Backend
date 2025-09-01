package com.taptalk.aimap.dto.quest;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
public class QuestInfo {
    private UUID id;
    private String title;
    private String desc;
    private String category;
    private String difficulty;
    private Integer avgMinutes;
    private String needPeople;
    private String areaCode;
    private String sigunguCode;
}

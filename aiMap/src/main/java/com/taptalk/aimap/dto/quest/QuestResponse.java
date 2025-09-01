package com.taptalk.aimap.dto.quest;

import com.taptalk.aimap.domain.quest.Quest;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class QuestResponse {
    private List<DayQuest> quests;

    @Data
    @Builder
    public static class DayQuest {
        private String date;
        private List<QuestInfo> recommendations;
    }

    @Data
    @Builder
    public static class QuestInfo {
        private UUID id;
        private String title;
        private String desc;
        private String category;
        private String difficulty;
        private Integer avgMinutes;
        private String needPeople;
        private String areaCode;
        private String sigunguCode;

        public static QuestInfo from(Quest quest) {
            return QuestInfo.builder()
                    .id(quest.getId())
                    .title(quest.getTitle())
                    .desc(quest.getDescription())
                    .category(quest.getCategory().getDescription())
                    .difficulty(quest.getDifficulty().getDescription())
                    .avgMinutes(quest.getAvgMinutes())
                    .needPeople(quest.getNeedPeople().getValue())
                    .areaCode(quest.getAreaCode())
                    .sigunguCode(quest.getSigunguCode())
                    .build();
        }
    }
}

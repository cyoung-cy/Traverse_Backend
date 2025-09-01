package com.taptalk.aimap.mapper;

import com.taptalk.aimap.domain.quest.Quest;
import com.taptalk.aimap.dto.quest.QuestResponse;
import org.springframework.stereotype.Component;

@Component
public class QuestMapper {

    public QuestResponse.QuestInfo toDto(Quest quest) {
        return QuestResponse.QuestInfo.builder()
                .id(quest.getId())
                .title(quest.getTitle())
                .desc(quest.getDescription())
                .category(quest.getCategory().getDescription())
                .difficulty(quest.getDifficulty().name())
                .avgMinutes(quest.getAvgMinutes())
                .needPeople(quest.getNeedPeople().name())
                .areaCode(quest.getAreaCode())
                .sigunguCode(quest.getSigunguCode())
                .build();
    }
}


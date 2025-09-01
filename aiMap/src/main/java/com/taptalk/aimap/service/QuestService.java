package com.taptalk.aimap.service;

import com.taptalk.aimap.domain.quest.Quest;
import com.taptalk.aimap.dto.quest.QuestResponse;
import com.taptalk.aimap.dto.quest.QuestSearchRequest;
import com.taptalk.aimap.mapper.QuestMapper;
import com.taptalk.aimap.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestMapper questMapper;

    /**
     * 단순 조회: 해당 위치에 해당하는 모든 퀘스트 반환
     */
    @Transactional(readOnly = true)
    public QuestResponse searchQuests(QuestSearchRequest request) {
        List<QuestResponse.DayQuest> dayQuests = new ArrayList<>();

        for (QuestSearchRequest.DayPlan dayPlan : request.getPlaces()) {
            Set<String> sigunguCodes = dayPlan.getLocations().stream()
                    .map(QuestSearchRequest.Location::getSigunguCode)
                    .collect(Collectors.toSet());

            List<Quest> quests = questRepository.findBySigunguCodeIn(sigunguCodes);

            List<QuestResponse.QuestInfo> infos = quests.stream()
                    .map(questMapper::toDto)
                    .collect(Collectors.toList());

            dayQuests.add(QuestResponse.DayQuest.builder()
                    .date(dayPlan.getDate())
                    .recommendations(infos)
                    .build());
        }

        return QuestResponse.builder()
                .quests(dayQuests)
                .build();
    }

    /**
     * 추천 기능: 하루에 최대 5개의 랜덤 퀘스트 추천
     */
    @Transactional(readOnly = true)
    public QuestResponse getRecommendations(QuestSearchRequest request) {
        List<QuestResponse.DayQuest> dayQuests = new ArrayList<>();

        for (QuestSearchRequest.DayPlan dayPlan : request.getPlaces()) {
            Set<String> areaCodes = dayPlan.getLocations().stream()
                    .map(QuestSearchRequest.Location::getAreaCode)
                    .collect(Collectors.toSet());
            
            Set<String> sigunguCodes = dayPlan.getLocations().stream()
                    .map(QuestSearchRequest.Location::getSigunguCode)
                    .collect(Collectors.toSet());

            List<Quest> quests = questRepository.findByAreaCodeInAndSigunguCodeIn(areaCodes, sigunguCodes);

            // 중복 제거 후 무작위로 최대 5개 추출
            List<Quest> randomSelected = quests.stream()
                    .distinct()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                Collections.shuffle(list);
                                return list.stream().limit(5).collect(Collectors.toList());
                            }
                    ));

            List<QuestResponse.QuestInfo> infos = randomSelected.stream()
                    .map(questMapper::toDto)
                    .collect(Collectors.toList());

            dayQuests.add(QuestResponse.DayQuest.builder()
                    .date(dayPlan.getDate())
                    .recommendations(infos)
                    .build());
        }

        return QuestResponse.builder()
                .quests(dayQuests)
                .build();
    }
}

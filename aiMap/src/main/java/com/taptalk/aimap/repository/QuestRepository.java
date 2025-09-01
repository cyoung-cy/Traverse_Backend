package com.taptalk.aimap.repository;

import com.taptalk.aimap.domain.quest.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface QuestRepository extends JpaRepository<Quest, UUID> {

    // 기존 단일 지역 쿼리
    List<Quest> findByAreaCodeAndSigunguCode(String areaCode, String sigunguCode);

    // 🔥 여러 지역코드(sigunguCode)를 한 번에 조회
    List<Quest> findBySigunguCodeIn(Set<String> sigunguCodes);

    // (선택) 지역코드와 시군구코드 조합 쿼리
    List<Quest> findByAreaCodeInAndSigunguCodeIn(Set<String> areaCodes, Set<String> sigunguCodes);
}

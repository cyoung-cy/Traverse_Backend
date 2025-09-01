package com.taptalk.aimap.domain.quest;

public enum QuestCategory {
    CULTURE("문화 체험"),
    HISTORY("역사/지식"),
    PHOTO("사진/영상"),
    LOCAL_COMM("로컬 소통"),
    EXPERIENCE("경험"),
    FOOD("맛집"),
    SIGHTSEEING("관광"),
    HEALING("힐링");

    private final String description;

    QuestCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

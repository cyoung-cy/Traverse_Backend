package com.taptalk.aimap.domain.quest;

public enum QuestDifficulty {
    EASY("하"),
    MEDIUM("중"),
    HARD("상");

    private final String description;

    QuestDifficulty(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 
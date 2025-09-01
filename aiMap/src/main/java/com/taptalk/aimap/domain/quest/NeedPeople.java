package com.taptalk.aimap.domain.quest;

public enum NeedPeople {
    SOLO("solo"),
    GROUP("group"),
    BOTH("both");

    private final String value;

    NeedPeople(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 
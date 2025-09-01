package com.taptalk.aimap.dto.quest;

import lombok.Data;
import java.util.List;

@Data
public class QuestSearchRequest {
    private List<DayPlan> places;

    @Data
    public static class DayPlan {
        private String date;
        private List<Location> locations;
    }

    @Data
    public static class Location {
        private String place;
        private String areaCode;
        private String sigunguCode;
    }
} 
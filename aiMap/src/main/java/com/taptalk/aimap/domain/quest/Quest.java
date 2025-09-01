package com.taptalk.aimap.domain.quest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quests")
@Getter
@Setter
@NoArgsConstructor
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestDifficulty difficulty;

    @Column(name = "avg_minutes", nullable = false)
    private Integer avgMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "need_people", nullable = false)
    private NeedPeople needPeople;

    @Column(name = "area_code", nullable = false)
    private String areaCode;

    @Column(name = "sigungu_code", nullable = false)
    private String sigunguCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
} 
package com.taptalk.aimap.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 날짜 관련 유틸리티 클래스
 */
public class DateUtils {
    /**
     * 두 날짜 사이의 일수를 계산
     */
    public static long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /**
     * 날짜가 유효한 범위 내에 있는지 확인
     */
    public static boolean isDateRangeValid(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate);
    }

    /**
     * 날짜가 미래인지 확인
     */
    public static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
} 
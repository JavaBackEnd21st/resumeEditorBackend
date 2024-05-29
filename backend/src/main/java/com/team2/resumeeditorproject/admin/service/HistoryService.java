package com.team2.resumeeditorproject.admin.service;

import java.time.LocalDate;
import java.util.Map;

public interface HistoryService {
    Map<String, Object> collectStatistics();
    void saveStatistics(Map<String, Object> statistics);

    Map<String, Object> getTotalTraffic();
    Map<String, Object> getProUserCnt();
    Map<String, Object> getTrafficForCurrentDate();
    Map<LocalDate, Integer> getTrafficData(LocalDate startDate, LocalDate endDate);
    Map<LocalDate, Integer> getDailyUserRegistrations(LocalDate startDate, LocalDate endDate);

    Map<String, Object> getTotalEdit();
    Map<String, Object> getRNumForCurrentDate();

    Map<String, Object> getTotalBoardCnt();

    Map<String, Object> getEditByMonthly();
    Map<String, Object> getEditByWeekly(String month);
    Map<String, Object> getEditByDaily(String startDate, String endDate);
}
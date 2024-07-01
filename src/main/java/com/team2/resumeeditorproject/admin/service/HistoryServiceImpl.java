package com.team2.resumeeditorproject.admin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.resumeeditorproject.admin.domain.History;
import com.team2.resumeeditorproject.admin.domain.Traffic;

import com.team2.resumeeditorproject.admin.dto.HistoryDTO;
import com.team2.resumeeditorproject.admin.repository.*;
import com.team2.resumeeditorproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{

    private final HistoryRepository historyRepository;
    private final AdminUserRepository userRepository;
    private final AdminResumeRepository resumeRepository;
    private final AdminResumeBoardRepository resumeBoardRepository;
    private final TrafficRepository trafficRepository;
    private final TrafficService trafficService;

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    private final AdminService adminService;

    /* 통계 수집 */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> collectStatistics() {
        Map<String, Object> statistics = new LinkedHashMap<>();

        Traffic todayTraffic = trafficRepository.findByInDate(LocalDate.now());
        if (todayTraffic != null) {
            statistics.put("traffic", todayTraffic.getVisitCount());
            statistics.put("edit_count", todayTraffic.getEditCount());
        }
        statistics.put("user_mode", getUserMode());
        statistics.put("user_status", getUserStatus());
        statistics.put("user_gender", getUserGender());
        statistics.put("user_age", getUserAge());

        Map<String, Map<String, Integer>> getRankOccuUser = getUserOccu();
        statistics.put("user_occu", getRankOccuUser.get("ranking_user"));

        Map<String, Map<String, Integer>> getRankCompUser = getUserComp();
        statistics.put("user_comp", getRankCompUser.get("ranking_user"));

        Map<String, Map<String, Integer>> getRankWishUser = getUserWish();
        statistics.put("user_wish", getRankWishUser.get("ranking_user"));

        Map<String, Object> getEditModeRatio = getEditMode();
        statistics.put("edit_mode", getEditModeRatio.get("edit_ratio"));

        Map<String, Object> getEditStatusRatio = getEditStatus();
        statistics.put("edit_status", getEditStatusRatio.get("edit_ratio"));

        Map<String, Object> getEditAgeRatio = getEditAge();
        statistics.put("edit_age", getEditAgeRatio.get("age_edit_ratio"));

        statistics.put("edit_date", getEditDate());

        Map<String, Map<String, Integer>> getRankOccuEdit = getEditOccu();
        statistics.put("edit_occu", getRankOccuEdit.get("ranking_resumeEdit"));

        Map<String, Map<String, Integer>> getRankCompEdit = getEditComp();
        statistics.put("edit_comp", getRankCompEdit.get("ranking_resumeEdit"));
        return statistics;
    }

    /* DB에 저장 */
    @Override
    @Transactional
    public void saveStatistics(Map<String, Object> statistics) {
        // Statistics 저장 로직
        try {

            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setTraffic((int) statistics.get("traffic"));
            historyDTO.setEdit_count((int) statistics.get("edit_count"));
            historyDTO.setUser_mode(objectMapper.writeValueAsString(statistics.get("user_mode")));
            historyDTO.setUser_status(objectMapper.writeValueAsString(statistics.get("user_status")));
            historyDTO.setUser_gender(objectMapper.writeValueAsString(statistics.get("user_gender")));
            historyDTO.setUser_age(objectMapper.writeValueAsString(statistics.get("user_age")));
            historyDTO.setUser_occu(objectMapper.writeValueAsString(statistics.get("user_occu")));
            historyDTO.setUser_comp(objectMapper.writeValueAsString(statistics.get("user_comp")));
            historyDTO.setUser_wish(objectMapper.writeValueAsString(statistics.get("user_wish")));
            historyDTO.setEdit_mode(objectMapper.writeValueAsString(statistics.get("edit_mode")));
            historyDTO.setEdit_status(objectMapper.writeValueAsString(statistics.get("edit_status")));
            historyDTO.setEdit_age(objectMapper.writeValueAsString(statistics.get("edit_age")));
            historyDTO.setEdit_date(objectMapper.writeValueAsString(statistics.get("edit_date")));
            historyDTO.setEdit_occu(objectMapper.writeValueAsString(statistics.get("edit_occu")));
            historyDTO.setEdit_comp(objectMapper.writeValueAsString(statistics.get("edit_comp")));
            historyDTO.setW_date(new java.util.Date());

            History history = modelMapper.map(historyDTO, History.class);

            // 어제의 날짜를 계산하여 traffic_date 컬럼에 저장
            LocalDate yesterday = LocalDate.now().minusDays(1);
            history.setTraffic_date(yesterday);

            historyRepository.save(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int resumeEditCnt(){
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return resumeRepository.findRNumByCurrentDate(currentDate);
    }

    /* 유저별 각 비율 */
    private Map<String, Object> getUserMode() {
        return adminService.modeCnt();
    }

    private Map<String, Object> getUserStatus(){
        return adminService.statusCnt();
    }

    private Map<String, Object> getUserGender(){
        return adminService.genderCnt();
    }

    private Map<String, Object> getUserAge(){
        return adminService.ageCnt();
    }

    private Map<String, Map<String, Integer>> getUserOccu(){
        return adminService.rankOccup();
    }

    private Map<String, Map<String, Integer>> getUserComp(){
        return adminService.rankComp();
    }

    private Map<String, Map<String, Integer>> getUserWish(){
        return  adminService.rankWish();
    }

    /* OO별 첨삭 비율 */
    private Map<String, Object> getEditMode() {
        return adminService.resumeEditCntByMode();
    }

    private Map<String, Object> getEditStatus() {
        return adminService.resumeEditCntByStatus();
    }

    private Map<String, Object> getEditAge() {
        return adminService.resumeEditCntByAge();
    }

    private Map<String, Object> getEditDate() {
        Map<String, Object> result = new LinkedHashMap<>();

        // 월별 첨삭 비율 가져오기
        Map<String, Object> monthlyData = adminService.resumeCntByMonth();
        Map<String, Object> monthlyRatios = (Map<String, Object>) monthlyData.get("edit_ratio");

        // 주차별 첨삭 비율 가져오기
        Map<String, Object> weeklyData = adminService.resumeCntByWeekly();
        Map<String, Object> weeklyRatios = (Map<String, Object>) weeklyData.get("edit_ratio");

        // 일별 첨삭 비율 가져오기
        Map<String, Object> dailyData = adminService.resumeCntByDaily();
        Map<String, Object> dailyRatios = (Map<String, Object>) dailyData.get("edit_ratio");

        // 통합 결과 생성
        result.put("monthly_ratio", monthlyRatios);
        result.put("weekly_ratio", weeklyRatios);
        result.put("daily_ratio", dailyRatios);

        return result;
    }

    private Map<String, Map<String, Integer>> getEditOccu() {
        return adminService.rankOccup();
    }

    private Map<String, Map<String, Integer>> getEditComp() {
        return adminService.rankComp();
    }

    // ---------------------------------------------
    // 통계 데이터 출력
    /* 프로 유저 수 */
    @Override
    public Map<String, Object> getProUserCnt() {
        Map<String, Object> result = new HashMap<>();

        int proUser = userRepository.findByMode(2).size();

        result.put("pro", proUser);

        return result;
    }

    /* 총 방문자 수 */
    @Override
    public Map<String, Object> getTotalTraffic() {
        Map<String, Object> result = new HashMap<>();
        long totalTraffic = trafficService.getTotalTraffic();
        result.put("total_visit", totalTraffic);
        return result;
    }

    /* 오늘 방문자 수 */
    @Override
    public Map<String, Object> getTrafficForCurrentDate() {
        Map<String, Object> result = new HashMap<>();
        long todayTrafficFromDb = trafficService.getTrafficForCurrentDate();
        result.put("today_visit", todayTrafficFromDb);
        return result;
    }


    /* 일별 회원가입 집계 */
    @Override
    public Map<LocalDate, Integer> getDailyUserRegistrations(LocalDate startDate, LocalDate endDate) {
        // startDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay();

        // endDate를 다음 날의 시작으로 LocalDateTime으로 변환
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // userRepository.findByInDateBetween() 메서드에 LocalDateTime 인스턴스를 전달
        List<User> users = userRepository.findByInDateBetween(startDateTime, endDateTime);

        Map<LocalDate, Integer> dailyRegistrations = new HashMap<>();
        for (User user : users) {
            LocalDate registrationDate = user.getInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dailyRegistrations.merge(registrationDate, 1, Integer::sum);
        }

        // 날짜별로 정렬된 맵 반환
        return new TreeMap<>(dailyRegistrations);
    }

    /* 월별 회원가입 집계 */
    @Override
    public Map<LocalDate, Integer> getMonthlyUserRegistrations(YearMonth yearMonth) {
        Map<LocalDate, Integer> monthlySignupData = new TreeMap<>();

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<User> signupList = userRepository.findByInDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        // 해당 월의 각 날짜에 대한 회원가입 데이터 집계
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            final LocalDate currentDate = date;

            // 해당 날짜에 대한 회원가입 수 계산
            int signupCount = (int) signupList.stream()
                    .filter(user -> {
                        LocalDate userDate = user.getInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return userDate.isEqual(currentDate);
                    })
                    .count();
            monthlySignupData.put(currentDate, signupCount);
        }
        return monthlySignupData;
    }

    /* 총 첨삭 수 */
    @Override
    public Map<String, Object> getTotalEdit() {
        Map<String, Object> result = new LinkedHashMap<>();

        long editCount = resumeRepository.count();

        result.put("edit_count", editCount);

        return result;
    }

    /* 오늘 첨삭 수 */
    @Override
    public Map<String, Object> getRNumForCurrentDate() {
        Map<String, Object> result = new LinkedHashMap<>();

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        long editCount = resumeRepository.findRNumByCurrentDate(currentDate);

        result.put("edit_count", editCount);

        return result;
    }

    /* 총 게시글 수 */
    @Override
    public Map<String, Object> getTotalBoardCnt() {
        Map<String, Object> result = new HashMap<>();

        long totalBoardCnt = resumeBoardRepository.count();
        result.put("total_board", totalBoardCnt);

        return result;
    }

    /* 월별 첨삭 집계 */
    @Override
    public Map<String, Object> getEditByMonthly() {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Integer> editCounts = new LinkedHashMap<>();
        Map<String, Double> editRatios = new LinkedHashMap<>();


        List<Object[]> monthList = resumeRepository.findMonthlyCorrectionCounts();

        // 전체 첨삭 횟수 계산
        int totalCorrections = monthList.stream().mapToInt(row -> ((Number) row[1]).intValue()).sum();

        // 월별 첨삭 횟수 및 비율 계산
        for (Object[] row : monthList) {
            String month = (String) row[0];
            int monthCnt = ((Number) row[1]).intValue();
            double monthRatio = ((double) monthCnt / totalCorrections) * 100;

            editCounts.put(month, monthCnt);
            editRatios.put(month, Math.round(monthRatio * 100) / 100.0);
        }

        Map<String, Object> combinedCounts = new LinkedHashMap<>();
        Map<String, Object> combinedRatios = new LinkedHashMap<>();

        result.put("edit_cnt", editCounts);
        result.put("edit_ratio", editRatios);

        return result;
    }

    /* 주별 첨삭 집계 */
    @Override
    public Map<String, Object> getEditByWeekly(String month) {
        Map<String, Object> result = new LinkedHashMap<>();

        // 월별 첨삭 데이터 가져오기
        Map<String, Object> editMonthly = adminService.resumeCntByMonth();

        // month가 null 또는 빈 값일 경우 현재 달로 설정
        if (month == null || month.isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            month = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        boolean dataExists = false;

        // edit_cnt에서 주어진 month의 weekly 데이터 추출
        if (editMonthly != null) {
            Map<String, Object> editCnt = (Map<String, Object>) editMonthly.get("edit_cnt");
            if (editCnt != null && editCnt.containsKey("weekly")) {
                Map<String, Object> editCntWeekly = (Map<String, Object>) editCnt.get("weekly");
                if (editCntWeekly != null && editCntWeekly.containsKey(month)) {
                    Map<String, Object> editCntResult = new LinkedHashMap<>();
                    editCntResult.put(month, editCntWeekly.get(month));
                    result.put("edit_cnt", editCntResult);
                    dataExists = true;
                }
            }

            // edit_ratio에서 주어진 month의 weekly 데이터 추출
            Map<String, Object> editRatio = (Map<String, Object>) editMonthly.get("edit_ratio");
            if (editRatio != null && editRatio.containsKey("weekly")) {
                Map<String, Object> editRatioWeekly = (Map<String, Object>) editRatio.get("weekly");
                if (editRatioWeekly != null && editRatioWeekly.containsKey(month)) {
                    Map<String, Object> editRatioResult = new LinkedHashMap<>();
                    editRatioResult.put(month, editRatioWeekly.get(month));
                    result.put("edit_ratio", editRatioResult);
                    dataExists = true;
                }
            }
        }

        if (!dataExists) {
            throw new IllegalArgumentException("해당 월의 데이터가 없습니다.");
        }

        return result;
    }

    /* 일별 첨삭 집계 */
    @Override
    public Map<String, Object> getEditByDaily(String startDate, String endDate) {
        Map<String, Object> editDaily = adminService.resumeCntByDaily();
        Map<String, Object> result = new LinkedHashMap<>();

        // startDate와 endDate가 주어지지 않은 경우 현재 날짜를 기준으로 -6(총 7일)의 데이터를 설정
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : LocalDate.now().minusDays(6);
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : LocalDate.now();

        // "edit_cnt" 데이터 처리
        Map<String, Integer> editCntByDate = (Map<String, Integer>) editDaily.get("edit_cnt");
        Map<String, Integer> filteredEditCntData = new LinkedHashMap<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dateString = date.toString();
            filteredEditCntData.put(dateString, editCntByDate.getOrDefault(dateString, 0));
        }

        // "edit_ratio" 데이터 처리
        Map<String, Double> editRatioByDate = (Map<String, Double>) editDaily.get("edit_ratio");
        Map<String, Double> filteredEditRatioData = new LinkedHashMap<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            String dateString = date.toString();
            filteredEditRatioData.put(dateString, editRatioByDate.getOrDefault(dateString, 0.0));
        }

        // 결과에 데이터 추가
        result.put("edit_cnt", filteredEditCntData);
        result.put("edit_ratio", filteredEditRatioData);

        return result;
    }
}
package com.team2.resumeeditorproject.admin.service;

import java.util.Map;

//통계 관련 service
public interface AdminService {

    Map<String, Object> userCnt();
    Map<String, Object> genderCnt();
    Map<String, Object> occupCnt(String occupation);
    Map<String, Object> wishCnt(String wish);

    Map<String, Map<String, Integer>> rankOccup();
    Map<String, Map<String, Integer>> rankComp();
    Map<String, Map<String, Integer>> rankWish();

    Map<String, Object> ageCnt();
    Map<String, Object> statusCnt();
    Map<String, Object> modeCnt();

    Map<String, Object> CompResumeCnt(String company);
    Map<String, Object> OccupResumeCnt(String occupation);

    Map<String, Object> resumeEditCntByStatus();
    Map<String, Object> resumeEditCntByOccup(String occupation);
    Map<String, Object> resumeEditCntByComp(String company);
    Map<String, Object> resumeEditCntByAge();
    Map<String, Object> resumeEditCntByMode();

    Map<String, Object> resumeCntByMonth();
    Map<String, Object> resumeCntByDaily();
    Map<String, Object> resumeCntByWeekly();

}

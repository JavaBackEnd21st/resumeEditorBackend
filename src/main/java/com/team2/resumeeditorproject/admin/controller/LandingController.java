package com.team2.resumeeditorproject.admin.controller;

import com.team2.resumeeditorproject.admin.service.AdminService;
import com.team2.resumeeditorproject.admin.service.HistoryService;
import com.team2.resumeeditorproject.admin.service.ReviewManagementService;
import com.team2.resumeeditorproject.review.domain.Review;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.team2.resumeeditorproject.admin.service.ResponseHandler.createBadRequestResponse;
import static com.team2.resumeeditorproject.admin.service.ResponseHandler.createOkResponse;

@Controller
@RequestMapping("/landing")
@RequiredArgsConstructor
public class LandingController {

    private final AdminService adminService;
    private final HistoryService historyService;
    private final ReviewManagementService reviewService;

    @GetMapping("/statistics")
    public ResponseEntity<Map<String,Object>> getStatistics(@RequestParam(name="group", required=false) String group){
        Function<String, ResponseEntity<Map<String, Object>>> action = switch (group) {
            case "countUser" -> (g) -> createOkResponse(adminService.userCnt());
            case "visitTotal" -> (g) -> createOkResponse(historyService.getTotalTraffic());
            case "editTotal" -> (g) -> createOkResponse(historyService.getTotalEdit());
            case "boardTotal" -> (g) -> createOkResponse(historyService.getTotalBoardCnt());
            default -> (g) ->  createBadRequestResponse("잘못된 요청입니다.");
        };
        return action.apply(group);
    }

    @GetMapping("/review")
    public ResponseEntity<Map<String,Object>> getAllVisibleReviews() {
        try {
            List<Review> reviews = reviewService.getVisibleReviews();

            Map<String, Object> response = new HashMap<>();
            response.put("review", reviews);

            return createOkResponse(response);
        }catch(Exception e){
            return createBadRequestResponse(e.getMessage());
        }

    }
}

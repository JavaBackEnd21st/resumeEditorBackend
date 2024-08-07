package com.team2.resumeeditorproject.admin.controller;

import com.team2.resumeeditorproject.admin.dto.request.SearchUserRequest;
import com.team2.resumeeditorproject.admin.dto.response.UserListResponse;
import com.team2.resumeeditorproject.admin.service.UserDeleteService;
import com.team2.resumeeditorproject.admin.service.UserManagementService;
import com.team2.resumeeditorproject.common.dto.response.CommonResponse;
import com.team2.resumeeditorproject.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final UserDeleteService userDeleteService;

    private static final int SIZE_OF_PAGE = 20; // 한 페이지에 보여줄 회원 수

    /* 회원 목록 */
    @GetMapping("/list")
    public ResponseEntity<UserListResponse> getUserList(
            @RequestParam(defaultValue = "0", name = "pageNo") int pageNo) {

        Page<UserDTO> userPage = userManagementService.getUserList(pageNo, SIZE_OF_PAGE);

        return ResponseEntity.ok()
                .body(UserListResponse.builder()
                        .totalPage(userPage.getTotalPages())
                        .userPage(userPage.getContent())
                        .build());
    }

    /* 회원 검색 */
    @GetMapping("/list/search")
    public ResponseEntity<UserListResponse> searchUsers(@ModelAttribute SearchUserRequest searchUserRequest) {

        Page<UserDTO> userPage = userManagementService.searchUsersByGroupAndKeyword(searchUserRequest, SIZE_OF_PAGE);

        return ResponseEntity.ok()
                .body(UserListResponse.builder()
                        .totalPage(userPage.getTotalPages())
                        .userPage(userPage.getContent())
                        .build());
    }

    /* 회원 탈퇴 */
    @DeleteMapping("/{userNo}")
    public ResponseEntity<CommonResponse<String>> deleteUser(@PathVariable("userNo") long userNo) {
        userDeleteService.deleteUser(userNo);

        return ResponseEntity.ok()
                .body(CommonResponse.<String>builder()
                        .response("User deleted successfully")
                        .status("Success")
                        .time(new Date())
                        .build());
    }
}

package com.team2.resumeeditorproject.user.service;

import com.team2.resumeeditorproject.user.domain.User;
import com.team2.resumeeditorproject.user.dto.UserDTO;

public interface UserService {
    // 회원가입
    void signup(UserDTO userDto);

    Boolean checkUsernameDuplicate(String username);
    Boolean checkEmailDuplicate(String email);

    int updateUserMode(long u_num);
    User showUser(String username);

    //회원탈퇴
    void deleteUser(Long uNum);

    Boolean checkUserExist(Long uNum);
    //회원정보 수정
    void updateUser(UserDTO userDto);

    // 사용자 조회
    User findUser(Long uNum);

    // User 정보 저장
    void saveUser(User user);
}

package com.team2.resumeeditorproject.user.service;

import com.team2.resumeeditorproject.user.dto.UserDTO;

public interface SignupService {
	void signup(UserDTO userDto);

	@Deprecated
	void checkUsernameDuplicate(String username);

	@Deprecated
	void checkCanRejoinAfterWithdrawal(String email);
}

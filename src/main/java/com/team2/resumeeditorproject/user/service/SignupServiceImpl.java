package com.team2.resumeeditorproject.user.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.team2.resumeeditorproject.common.ServiceSpecConstants;
import com.team2.resumeeditorproject.company.repository.CompanyRepository;
import com.team2.resumeeditorproject.exception.DelDateException;
import com.team2.resumeeditorproject.occupation.repository.OccupationRepository;
import com.team2.resumeeditorproject.user.domain.User;
import com.team2.resumeeditorproject.user.dto.UserDTO;
import com.team2.resumeeditorproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

	private final OccupationRepository occupationRepository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void signup(UserDTO userDTO) {
		//회원가입 진행
		User user = User.builder()
				.email(userDTO.getEmail())
				.username(userDTO.getUsername())
				.password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
				.role("ROLE_USER")
				.birthDate(userDTO.getBirthDate())
				.age(userDTO.getAge())
				.gender(userDTO.getGender())
				.occupation(occupationRepository.findById(userDTO.getOccupationNo()).orElseThrow(() -> new IllegalArgumentException("Invalid Occupation No: " + userDTO.getOccupationNo())))
				.company(companyRepository.findById(userDTO.getCompanyNo()).orElseThrow(() -> new IllegalArgumentException("Invalid Company No: " + userDTO.getCompanyNo())))
				.wishCompany(companyRepository.findById(userDTO.getWishCompanyNo()).orElseThrow(() -> new IllegalArgumentException("Invalid Company No: " + userDTO.getWishCompanyNo())))
				.status(userDTO.getStatus())
				.mode(1)
				.build();
		userRepository.save(user);
	}

	@Override
	public void checkUsernameDuplicate(String username) {
		if (userRepository.existsByUsername(username)) {
			throw new IllegalStateException("username: " + username + " already exists");
		}
	}

	@Override
	public void checkCanRejoinAfterWithdrawal(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isEmpty()) {
			return;
		}

		LocalDateTime deletedDate = optionalUser.get().getDeletedDate();

		if (deletedDate == null) {
			return;
		}

		LocalDateTime dateCanRejoin = deletedDate.plusDays(ServiceSpecConstants.DATE_CAN_REJOIN_AFTER_WITHDRAWAL);

		if (dateCanRejoin.isAfter(LocalDateTime.now())) {
			return;
		}

		List<String> result = new ArrayList<>();
		result.add(deletedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
		result.add(dateCanRejoin.format(DateTimeFormatter.ISO_LOCAL_DATE));
		throw new DelDateException(result);
	}
}

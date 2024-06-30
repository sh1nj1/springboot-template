package com.vrerv.springboottemplate.server.feature.user;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
public class UserFixture {

	private final UserRepo userRepository;

	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserCsv {
		@CsvBindByName(column = "account")
		private String account;
		@CsvBindByName(column = "email")
		private String email;
	}

	@PostConstruct
	public void init() {
		if (userRepository.count() > 0) {
			return;
		}

		try (Reader reader = new InputStreamReader(new ClassPathResource("fixture/users.csv").getInputStream())) {
			CsvToBean<UserCsv> csvToBean = new CsvToBeanBuilder<UserCsv>(reader)
					.withType(UserCsv.class)
					.build();
			List<User> users = csvToBean.parse().stream()
					.map(it -> User.builder().account(it.getAccount()).password("1234").build())
					.collect(Collectors.toList());
			userRepository.saveAll(users);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

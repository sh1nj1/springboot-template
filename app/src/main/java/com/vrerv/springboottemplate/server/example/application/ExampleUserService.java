package com.vrerv.springboottemplate.server.example.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.vrerv.springboottemplate.server.example.application.model.ExampleUserCreateModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserResponseModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserUpdateModel;
import com.vrerv.springboottemplate.server.example.domain.ExampleContact;
import com.vrerv.springboottemplate.server.example.domain.ExampleContactRepo;
import com.vrerv.springboottemplate.server.example.domain.ExampleUser;
import com.vrerv.springboottemplate.server.example.domain.ExampleUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Transactional
@Service
public class ExampleUserService {

	private final ExampleUserRepo repo;
	private final ExampleContactRepo contactRepo;
	private final ExampleUserMapper mapper;

	@Transactional(readOnly = true)
	public Page<ExampleUserResponseModel> list(Pageable pageable) {

		log.info("list={}", repo.findAll(pageable).map(mapper::toDto).stream().collect(Collectors.toList()));
		return repo.findAll(pageable).map(mapper::toDto);
	}

	public ExampleUserResponseModel create(@Valid ExampleUserCreateModel dto) {

		ExampleUser entity = mapper.toEntity(dto);
		ExampleUser newEntity = repo.save(entity);
		List<ExampleContact> contacts = mapper.toContactEntities(dto.getContacts());
		contacts.forEach(it -> it.setUser(newEntity));
		contactRepo.saveAll(contacts);
		newEntity.setContacts(contacts);
		return mapper.toDto(repo.save(newEntity));
	}

	public ExampleUserResponseModel update(Long id, @Valid ExampleUserUpdateModel dto) {

		ExampleUser exampleUser = repo.findById(id).orElseThrow(EntityNotFoundException::new);
		mapper.updateEntity(exampleUser, dto);
		return mapper.toDto(exampleUser);
	}

	public void delete(Long id) {

		repo.deleteById(id);
	}
}

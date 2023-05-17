package com.vrerv.springboottemplate.server.example.application;

import java.util.List;

import com.vrerv.springboottemplate.server.example.application.model.ExampleUserContactModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserCreateModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserResponseModel;
import com.vrerv.springboottemplate.server.example.application.model.ExampleUserUpdateModel;
import com.vrerv.springboottemplate.server.example.domain.ExampleContact;
import com.vrerv.springboottemplate.server.example.domain.ExampleUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ExampleUserMapper {

	ExampleUserResponseModel toDto(ExampleUser entity);

	@Mapping(target = "contacts", ignore = true)
	ExampleUser toEntity(ExampleUserCreateModel dto);
	void updateEntity(@MappingTarget ExampleUser entity, ExampleUserUpdateModel dto);

	ExampleContact toContactEntity(ExampleUserContactModel dto);

	List<ExampleContact> toContactEntities(List<ExampleUserContactModel> dto);
}

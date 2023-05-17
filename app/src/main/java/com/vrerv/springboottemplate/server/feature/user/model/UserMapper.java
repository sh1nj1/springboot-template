package com.vrerv.springboottemplate.server.feature.user.model;

import com.vrerv.springboottemplate.server.feature.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {

	UserModel toDto(User entity);

	User toEntity(UserModel dto);
}

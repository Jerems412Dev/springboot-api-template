package com.jeremsdev.validations.mapper;

import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE =  Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO userDTO);
}

package com.jeremsdev.validations.mapper;

import com.jeremsdev.validations.dto.BookDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE =  Mappers.getMapper(BookMapper.class);

    BookDTO toDTO(Book book);
    @Mapping(target = "loans", ignore = true)
    Book toEntity(BookDTO bookDTO);
    @Mapping(target = "loans", ignore = true)
    void updateEntityFromDTO(BookDTO bookDTO, @MappingTarget Book book);
}

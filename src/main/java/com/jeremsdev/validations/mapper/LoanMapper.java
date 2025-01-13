package com.jeremsdev.validations.mapper;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanMapper {
    LoanMapper INSTANCE =  Mappers.getMapper(LoanMapper.class);

    @Mapping(target = "idBook", source = "books")
    @Mapping(target = "idUser", source = "users")
    LoanDTO toDTO(Loan loan);
    @Mapping(target = "books", source = "idBook")
    @Mapping(target = "users", source = "idUser")
    Loan toEntity(LoanDTO loanDTO);
    void updateEntityFromDTO(LoanDTO loanDTO, @MappingTarget Loan loan);
}

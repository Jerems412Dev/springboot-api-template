package com.jeremsdev.validations.mapper;

import com.jeremsdev.validations.dto.LoanDTO;
import com.jeremsdev.validations.dto.UserDTO;
import com.jeremsdev.validations.model.Book;
import com.jeremsdev.validations.model.Loan;
import com.jeremsdev.validations.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {BookMapper.class, UserMapper.class})
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    // Conversion Loan -> LoanDTO
    @Mapping(target = "idBook", source = "book.idBook")
    @Mapping(target = "idUser", source = "user.idUser")
    LoanDTO toDTO(Loan loan);

    // Conversion LoanDTO -> Loan
    @Mapping(target = "book", source = "idBook", qualifiedByName = "mapIdToBook")
    @Mapping(target = "user", source = "idUser", qualifiedByName = "mapIdToUser")
    Loan toEntity(LoanDTO loanDTO);

    // Mise à jour d'une entité existante avec les données du DTO
    @Mapping(target = "book", source = "idBook", qualifiedByName = "mapIdToBook")
    @Mapping(target = "user", source = "idUser", qualifiedByName = "mapIdToUser")
    void updateEntityFromDTO(LoanDTO loanDTO, @MappingTarget Loan loan);

    // Méthodes utilitaires pour mapper les relations
    @Named("mapIdToBook")
    default Book mapIdToBook(Long idBook) {
        if (idBook == null) return null;
        Book book = new Book();
        book.setIdBook(idBook);
        return book;
    }

    @Named("mapIdToUser")
    default User mapIdToUser(Long idUser) {
        if (idUser == null) return null;
        User user = new User();
        user.setIdUser(idUser);
        return user;
    }
}

package me.elamranioussama.bankaccountgraphqlservice.mappers;

import me.elamranioussama.bankaccountgraphqlservice.dtos.BankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.dtos.CreateBankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.entities.BankAccount;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountDTO toDTO(BankAccount entity);

    BankAccount toEntity(CreateBankAccountDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BankAccountDTO dto, @MappingTarget BankAccount entity);
}

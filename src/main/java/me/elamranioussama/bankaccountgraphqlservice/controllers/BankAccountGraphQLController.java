package me.elamranioussama.bankaccountgraphqlservice.controllers;

import me.elamranioussama.bankaccountgraphqlservice.dtos.BankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.dtos.CreateBankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.entities.BankAccount;
import me.elamranioussama.bankaccountgraphqlservice.mappers.BankAccountMapper;
import me.elamranioussama.bankaccountgraphqlservice.repositories.BankAccountRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class BankAccountGraphQLController {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountGraphQLController(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @QueryMapping
    public List<BankAccount> accountsList() {
        return bankAccountRepository.findAll();
    }

    @QueryMapping
    public BankAccount accountById(@Argument String id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Account %s not found", id)));
    }

    @MutationMapping
    public BankAccount addAccount(@Argument CreateBankAccountDTO bankAccount) {
        BankAccount account = bankAccountMapper.toEntity(bankAccount);
        if (account.getId() == null) {
            account.setId(UUID.randomUUID().toString());
        }
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(new Date());
        }
        return bankAccountRepository.save(account);
    }

    @MutationMapping
    public BankAccount updateAccount(@Argument String id, @Argument BankAccountDTO bankAccount) {
        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Account %s not found", id)));

        bankAccountMapper.updateEntityFromDTO(bankAccount, account);
        
        return bankAccountRepository.save(account);
    }

    @MutationMapping
    public Boolean deleteAccount(@Argument String id) {
        bankAccountRepository.deleteById(id);
        return true;
    }
}

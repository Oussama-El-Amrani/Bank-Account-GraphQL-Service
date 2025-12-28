package me.elamranioussama.bankaccountgraphqlservice.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import me.elamranioussama.bankaccountgraphqlservice.dtos.BankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.dtos.CreateBankAccountDTO;
import me.elamranioussama.bankaccountgraphqlservice.entities.BankAccount;
import me.elamranioussama.bankaccountgraphqlservice.mappers.BankAccountMapper;
import me.elamranioussama.bankaccountgraphqlservice.repositories.BankAccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Tag(name = "Bank Account Management", description = "APIs for managing bank accounts")
public class AccountRestController {

    private BankAccountRepository bankAccountRepository;
    private final BankAccountMapper mapper;

    @GetMapping("/bankAccounts")
    public ResponseEntity<List<BankAccountDTO>> getAll(){
        List<BankAccountDTO> accounts = bankAccountRepository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
        return ResponseEntity.ok(accounts);
    }


    @GetMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> getById(
            @Parameter(description = "Unique identifier of the bank account", required = true)
            @PathVariable String id) {
        BankAccount entity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        return ResponseEntity.ok(mapper.toDTO(entity));
    }

    @PostMapping("/bankAccounts")
    public ResponseEntity<BankAccountDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Bank account details (ID and createdAt are auto-generated)",
                    required = true)
            @RequestBody CreateBankAccountDTO dto) {
        BankAccount entity = mapper.toEntity(dto);
        entity.setId(java.util.UUID.randomUUID().toString());
        entity.setCreatedAt(new java.util.Date());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toDTO(bankAccountRepository.save(entity)));
    }

    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> update(
            @Parameter(description = "Unique identifier of the bank account", required = true)
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated account details (only include fields to update)",
                    required = true)
            @RequestBody BankAccountDTO dto) {
        BankAccount existing = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        mapper.updateEntityFromDTO(dto, existing);
        return ResponseEntity.ok(mapper.toDTO(bankAccountRepository.save(existing)));
    }

    @DeleteMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> delete(
            @Parameter(description = "Unique identifier of the bank account to delete", required = true)
            @PathVariable String id){
        BankAccount existing = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        bankAccountRepository.delete(existing);
        return ResponseEntity.ok(mapper.toDTO(existing));
    }
}
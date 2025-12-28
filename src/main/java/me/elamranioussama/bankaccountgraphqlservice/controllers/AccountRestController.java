package me.elamranioussama.bankaccountgraphqlservice.controllers;


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
            @PathVariable String id) {
        BankAccount entity = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        return ResponseEntity.ok(mapper.toDTO(entity));
    }

    @PostMapping("/bankAccounts")
    public ResponseEntity<BankAccountDTO> create(
            @RequestBody CreateBankAccountDTO dto) {
        BankAccount entity = mapper.toEntity(dto);
        entity.setId(java.util.UUID.randomUUID().toString());
        entity.setCreatedAt(new java.util.Date());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toDTO(bankAccountRepository.save(entity)));
    }

    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> update(
            @PathVariable String id,
            @RequestBody BankAccountDTO dto) {
        BankAccount existing = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        mapper.updateEntityFromDTO(dto, existing);
        return ResponseEntity.ok(mapper.toDTO(bankAccountRepository.save(existing)));
    }

    @DeleteMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> delete(
            @PathVariable String id){
        BankAccount existing = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        bankAccountRepository.delete(existing);
        return ResponseEntity.ok(mapper.toDTO(existing));
    }
}
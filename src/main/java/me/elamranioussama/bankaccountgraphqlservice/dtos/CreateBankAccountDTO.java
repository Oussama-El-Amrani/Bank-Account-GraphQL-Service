package me.elamranioussama.bankaccountgraphqlservice.dtos;
import lombok.Data;
import me.elamranioussama.bankaccountgraphqlservice.enums.AccountType;

import java.util.Date;

@Data
public class CreateBankAccountDTO {
    private Date createdAt;
    private double balance;
    private String currency;
    private AccountType type;
}

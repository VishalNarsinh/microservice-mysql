package com.vishal.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts",description = "Schema to hold Account information")
public class AccountsDto {

    @Schema(description = "Account number of the Bank account", example = "1234567890")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Account number must be 10 digits")
    @NotBlank
    private Long accountNumber;

    @Schema(description = "Account type of Bank account", example = "Savings")
    @NotBlank(message = "Account type cannot be empty")
    private String accountType;

    @Schema(description = "Branch address of Bank account", example = "Andheri East, Mumbai")
    @NotBlank(message = "Branch address cannot be empty")
    private String branchAddress;
}

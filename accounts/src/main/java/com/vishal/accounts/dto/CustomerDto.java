package com.vishal.accounts.dto;

import com.vishal.accounts.validation.TrimmedNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold Customer & Account information"
)
public class CustomerDto {

    @Schema(description = "Name of the customer", example = "Vishal Narsinh")
    @TrimmedNotBlank(message = "The length of the customer name should be between 3 and 30 characters")
    private String name;

    @Schema(description = "Email address of the customer", example = "vishalnarsinh@gmail.com")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Please enter a valid email address")
    private String email;

    @Schema(description = "Mobile number of the customer", example = "9879879319")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(name = "Accounts", description = "Account details of the customer")
    private AccountsDto accountsDto;
}

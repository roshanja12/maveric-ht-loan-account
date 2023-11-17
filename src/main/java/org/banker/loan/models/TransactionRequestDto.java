package org.banker.loan.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    @Schema(required = true)

    @NotNull(message = "amount cannot be Blank")
    private BigDecimal amount;
    @Schema(required = true)
    @NotNull(message = "account id cannot be null")
    @Positive
    private Long accountId;

    @NotNull(message = "amount cannot be Blank")
    private Long loanId;

}

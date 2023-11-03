package org.banker.loan.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.File;
import java.math.BigDecimal;

@Data
@Schema(description = "Data for creating Loan account")
public class LoanDto {

    @NotBlank(message = "CustomerId cannot be Blank")
    @Schema(required = true,description = "Customer Id of the customer",type = SchemaType.NUMBER)
    private Long customerId;

    @NotBlank(message = "AccountId cannot be Blank")
    @Schema(required = true,description = "Account Id of the customer",type = SchemaType.NUMBER)
    private Long accountId;

    @NotBlank(message = "Amount cannot be Blank")
    @Schema(required = true,type = SchemaType.NUMBER)
    private BigDecimal amount;

    @NotBlank(message = "supportingDoc cannot be Blank")
    @Schema(required = true,type =SchemaType.OBJECT)
    private File supportingDoc;


}

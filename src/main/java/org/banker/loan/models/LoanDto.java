package org.banker.loan.models;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.io.File;
import java.math.BigDecimal;

@Data
@Schema(description = "Data for creating Loan account")
public class LoanDto {

    private Long loanId;

    @NotBlank(message = "CustomerId cannot be Blank")
    @Schema(required = true,description = "Customer Id of the customer",type = SchemaType.NUMBER)
    private Long customerId;

    @NotBlank(message = "AccountId cannot be Blank")
    @Schema(required = true,description = "Account Id of the customer",type = SchemaType.NUMBER)
    private Long savingsAccount;

    @NotBlank(message = "Amount cannot be Blank")
    @Schema(required = true,type = SchemaType.NUMBER)
    @Min(value = 100000,message ="Loan amount must be greater than one Lakh" )
    @Max(value = 5000000,message ="Loan amount must be less than Fifty Lakh" )
    private BigDecimal loanAmount;

    @NotBlank(message = "emi cannot be Blank")
    @Schema(required = true,type = SchemaType.NUMBER)
    @Min(value = 2,message ="Greater than 2 months" )
    private int emi;

    File file;



}

package org.banker.loan.models;

import lombok.Data;
import org.banker.loan.proxylayer.Customer;

@Data
public class LoanAccountDto {

    private Customer customerDetails;
    private String status;

}

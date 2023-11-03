package org.banker.loan.response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Global Response of the API")
@ApplicationScoped
public class LoanResponse {

    @Schema(required = true,description = "Http Status of an API",type = SchemaType.STRING)
    private String status;
    @Schema(required = true,description = "Http Code of an API",type = SchemaType.INTEGER)
    private int code;
    @Schema(required = true,description = "Success message of an API",type = SchemaType.STRING)
    private String msg;
    @Schema(required = true,description = "Error message of an API",type = SchemaType.STRING)
    private String errors;
    @Schema(required = true,description = "Path message of an API",type = SchemaType.STRING)
    private String path;
    @Schema(required = true,description = "CurrentDateTime message of an API")
    private LocalDateTime timestamp;
    @Schema(required = true,description = "Response message of an API",type = SchemaType.OBJECT)
    private Object data;
    @Schema(required = true,description = "data")
    @JsonbProperty
    private List<Loan> loans;
    @Schema(required = true,description = "paymenthistory")
    @JsonbProperty
    private List<LoanPaymentHistory> paymentHistory;

}

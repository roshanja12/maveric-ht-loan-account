package org.banker.loan.proxylayer;

import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private Long phoneNumber;
    @NotNull
    private String city;
    private Instant createdAt;
    private Instant modifiedAt;
    private String status;
}

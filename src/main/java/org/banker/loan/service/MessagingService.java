package org.banker.loan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.banker.loan.enums.Action;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.enums.Type;
import org.banker.loan.models.CreateLoanMessageDto;
import org.banker.loan.models.KafkaResponseDto;
import org.banker.loan.models.LoanDto;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.Instant;

public class MessagingService {

    @Inject
    @Channel("loan-event-producer")
    Emitter<KafkaResponseDto> messageEmitter;
    public void loanProducerCustom(LoanDto responseDetails){
        try {
            CreateLoanMessageDto messageDto =convertResponseDtoToEventMessage(responseDetails);
            KafkaResponseDto sendDto = new KafkaResponseDto();
            sendDto.setType(Type.LOAN_ACCOUNT_CREATED);
            sendDto.setMessage(messageDto);
            messageEmitter.send(sendDto);
        }catch (Exception e){
            e.getStackTrace();
        }

    }
    private CreateLoanMessageDto convertResponseDtoToEventMessage(LoanDto responseDetails) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CreateLoanMessageDto messageDto = new CreateLoanMessageDto();
            messageDto.setLoanId(responseDetails.getLoanId());
            messageDto.setAmount(responseDetails.getLoanAmount());
            messageDto.setType(Action.CREATE);
            messageDto.setCustomerId(responseDetails.getCustomerId().intValue());
            messageDto.setLoanStatus(LoanStatus.APPLIED);
            messageDto.setCreatedAt(Instant.now());
            return messageDto;

    }
}

package org.banker.loan.exception.Serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.banker.loan.models.KafkaResponseDto;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class LoanSerializer implements Serializer<KafkaResponseDto> {
    @Override
    public byte[] serialize(String topic, KafkaResponseDto createLoanMessageDto) {
        try {
            if (createLoanMessageDto == null){
                return null;
            }
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(createLoanMessageDto);
            objectStream.flush();
            objectStream.close();
            return byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
           return  null;
        }
    }

}


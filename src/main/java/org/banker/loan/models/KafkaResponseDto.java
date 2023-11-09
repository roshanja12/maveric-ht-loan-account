package org.banker.loan.models;

import lombok.Data;
import org.banker.loan.enums.Type;

import java.io.Serializable;

@Data
public class KafkaResponseDto implements Serializable {

    public Type type;
    public Object  message;

}

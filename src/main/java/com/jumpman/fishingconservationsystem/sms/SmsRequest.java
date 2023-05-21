package com.jumpman.fishingconservationsystem.sms;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SmsRequest {
    @NotBlank
    private final String phoneNumber; //destination phoneNumber
    @NotBlank
    private final String message ;   //actual message to be sent

    public SmsRequest(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}

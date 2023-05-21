package com.jumpman.fishingconservationsystem.sms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    private final SmsSender twilioSmsSender;

    @Autowired
    public SmsService(@Qualifier("twilio") TwilioSmsSender smsSender) {
        this.twilioSmsSender = smsSender;
    }

    public void sendSms(SmsRequest smsRequest){
        twilioSmsSender.sendSms(smsRequest);

    }
}

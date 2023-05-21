package com.jumpman.fishingconservationsystem.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class TwilioSmsSender implements SmsSender{
    private final TwilioConfig twilioConfig;
    @Autowired
    public TwilioSmsSender(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSms(SmsRequest smsRequest) {
        PhoneNumber to =new PhoneNumber(smsRequest.getPhoneNumber());
        PhoneNumber from=new PhoneNumber(twilioConfig.getTrialNumber());
        MessageCreator creator= Message.creator(to, from, smsRequest.getMessage());
        creator.create();
    }
}

package com.jumpman.fishingconservationsystem.exception;

import java.net.UnknownHostException;

public class EmailSendException extends UnknownHostException {

    public EmailSendException(String message) {
        super(message);
    }

    public EmailSendException() {
        super();
    }
}

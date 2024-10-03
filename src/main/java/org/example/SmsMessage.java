package org.example;

import lombok.Data;

@Data
class SmsMessage {
    private final String phoneNumber;
    private final String message;

    public SmsMessage(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}
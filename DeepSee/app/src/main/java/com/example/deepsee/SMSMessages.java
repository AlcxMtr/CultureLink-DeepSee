package com.example.deepsee;

public class SMSMessages {

    private String contactName;
    private String message;
    private String timestamp;

    public SMSMessages(String contactName, String message, String timestamp) {
        this.contactName = contactName;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getContactName() {
        return contactName;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessage(String shortBody) {
        this.message=shortBody;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp=timestamp;
    }
}


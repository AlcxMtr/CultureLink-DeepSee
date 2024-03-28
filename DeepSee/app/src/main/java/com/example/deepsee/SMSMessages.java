package com.example.deepsee;

import java.util.Date;

public class SMSMessages {

    private String contactName;
    private String message;
    private Date timestamp;

    public SMSMessages(String contactName, String message, Date timestamp) {
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setMessage(String shortBody) {
        this.message=shortBody;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp=timestamp;
    }
}


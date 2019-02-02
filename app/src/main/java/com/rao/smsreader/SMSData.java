package com.rao.smsreader;

public class SMSData {
    private String body;
    private String number;
    private Long date;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }


    public SMSData(String body, String number,Long date) {
        this.body = body;
        this.number = number;
        this.date=date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

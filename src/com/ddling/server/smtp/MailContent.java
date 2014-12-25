package com.ddling.server.smtp;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class MailContent {

    private String from;
    private String to;
    private String subject;
    private String content;

    private String mailContent;

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
        processMailContent();
    }

    public String getMailContent() {
        return mailContent;
    }

    private void processMailContent() {
        String contents[] = mailContent.split("\n\n");
        processHeader(contents[0]);
        this.setContent(contents[1]);
        System.out.println("Contents: " + getContent());
    }

    private void processHeader(String header) {
        String headers[] = header.split("\n");
        String subjects[] = headers[2].split(":");
        if (subjects[0].toLowerCase().equals("subject")) {
            subject = subjects[1];
            System.out.println("Subject: " + subject);
        }
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

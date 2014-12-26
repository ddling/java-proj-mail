package com.ddling.server.smtp;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class MailContent {

    // 邮件发送者
    private String from;
    // 邮件接受者
    private String to;
    // 邮件主题
    private String subject;
    // 邮件正文
    private String content;

    // 未加处理的邮件信息
    private String mailContent;

    /**
     * 处理邮件信息，并得到它的邮件头和邮件正文
     */
    private void processMailContent() {
        String contents[] = mailContent.split("\n\n");
        processHeader(contents[0]);
        this.setContent(contents[1]);
        System.out.println("Contents: " + getContent());
    }

    /**
     * 处理邮件头并分离出邮件发送者，邮件接收者和邮件主题
     * @param header 邮件头
     */
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

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
        processMailContent();
    }

    public String getMailContent() {
        return mailContent;
    }
}

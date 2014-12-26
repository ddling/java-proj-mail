package com.ddling.server.smtp;

import com.ddling.server.smtp.mx.MXExchange;
import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class SMTPClient {

    public Logger logger = LoggerFactory.getLogger(SMTPClient.class);

    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String server = null;
    private int port;
    private MailContent mailContent = null;

    public SMTPClient() {
        logger.info("Start a smtp client");
    }

    public SMTPClient(MailContent mailContent) {
        this.mailContent = mailContent;
        initilizeTheServer();
    }

    public void setServerAddressAndPort(String server, int port) {
        this.server = server;
        this.port = port;
    }

    private void initilizeTheServer() {
        int serverPos = mailContent.getTo().indexOf("@");
        server = mailContent.getTo().substring(serverPos + 1);

        if (server.equals("gmail.com")) {
            port = 465;
        } else {
            port = 25;
        }

        try {
            server = MXExchange.getMxServer(server);
        } catch (Exception e) {
            logger.error(e);
            server = null;
        }
    }

    public boolean sendEmail() {
        boolean sendEmailOK = true;

        try {
            initClient();
            ehlo();
            sendEmailHeader();
            sendEmailContent();
            quit();
        } catch (Exception e) {
            logger.error(e);
            sendEmailOK = false;
        } finally {
            this.closeTheClient();
        }

        return sendEmailOK;
    }

    private void quit() throws IOException {
        sendData("QUIT");

        int response = getResponse();

        if (response != 221) {
            throw new IOException("Quit fail");
        }

        logger.info("Quit Done!");
    }

    private void sendEmailContent() throws IOException{

        sendData("DATA");

        int response = getResponse();

        if (response != 354) {
            throw new IOException("Send Email content fail");
        }

        sendData(mailContent.getMailContent());
        sendData(".");

        response = getResponse();
        if (response != 250) {
            throw new IOException("Send Email Content fail");
        }

        logger.info("Send Email Content done!");
    }

    private void sendEmailHeader() throws IOException{

        sendData("MAIL FROM:<" + mailContent.getFrom() + ">");

        int response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        sendData("RCPT TO:<" + mailContent.getTo() + ">");

        response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        logger.info("Send Email Header done!");
    }

    private void ehlo() throws IOException{
        sendData("HELO " + server);

        int response = getResponse();

        if (response != 250) {
            throw new IOException("ehlo fail!");
        }

        logger.info("ehlo done!");
    }

    private void sendData(String s) {
        out.println(s);
        out.flush();
    }

    private void initClient() {
        logger.info("Connect to " + server + " At port " + port);

        try {
            socket = new Socket(server, port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            int response = getResponse();

            if (response == 220) {
                logger.info("Connect to " + server + "done!");
            }

        } catch (IOException e) {
            logger.error(e);
        }
    }

    private int getResponse() {

        String line = "";
        try {
            line = in.readLine();
        } catch (IOException e) {
            logger.error(e);
        }

        logger.debug(line);
        StringTokenizer get = new StringTokenizer(line, " ");
        return Integer.parseInt(get.nextToken());
    }

    public void closeTheClient() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
        MailContent mailContent = new MailContent();
        mailContent.setFrom("jtdx_159020@sohu.com");
        mailContent.setTo("465391062@qq.com");
        mailContent.setSubject("Hello");
        String body = "mail from:<" + mailContent.getFrom() + ">" + "\r\n";
        body += "rcpt to:<" + mailContent.getTo() + ">" + "\r\n";
        body += "Subject:This is a test email" + "\r\n";
        body += "\n";
        body += "Hello World!";
        System.out.println(body);
        mailContent.setMailContent(body);
        SMTPClient smtpClient = new SMTPClient(mailContent);
        smtpClient.sendEmail();
    }
}

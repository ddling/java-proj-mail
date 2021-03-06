/*
 * Copyright (C) 2014 lingdongdong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ddling.server.smtp;

import com.ddling.mailmanage.Mail;
import com.ddling.server.smtp.mx.MXExchange;
import com.ddling.utils.Constants;
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

    // 得到log实例
    public Logger logger = LoggerFactory.getLogger(SMTPClient.class);

    // 与服务器相连的Socket
    private Socket socket = null;
    // 从socket里面读取数据
    private BufferedReader in = null;
    // 往socket里面写入数据
    private PrintWriter out = null;
    // 得到合适的服务器地址
    private String server = null;
    // 与服务器地址相对应的端口号
    private int port;
    // 一条邮件信息，包含邮件头以及邮件正文
    private Mail mail = null;

    public SMTPClient() {
        logger.info("Start a smtp client");
    }

    public SMTPClient(Mail mail, int localOrForeign) {
        this.mail = mail;

        if (localOrForeign == Constants.CLIENT_SEND_TO_FOREIGN) {
            initilizeTheServer();
        } else if (localOrForeign == Constants.CLIENT_SEND_TO_LOCAL) {
            setServerAddressAndPort(Constants.LOCAL_SMTP_SERVER_ADDRESS, Constants.LOCAL_SMTP_SERVER_PORT);
        }
    }

    public void setServerAddressAndPort(String server, int port) {
        this.server = server;
        this.port = port;
    }

    /**
     * 初始化邮件服务器信息，包括得到邮件地址以及对应的端口号
     */
    private void initilizeTheServer() {
        int serverPos = mail.getTo().indexOf("@");
        server = mail.getTo().substring(serverPos + 1);

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

    /**
     * 发送邮件
     * @return 发送成功返回true， 发送失败返回false
     */
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

    /**
     * 向邮件服务器请求退出
     * @throws IOException
     */
    private void quit() throws IOException {
        sendData("QUIT");

        int response = getResponse();

        if (response != 221) {
            throw new IOException("Quit fail");
        }

        logger.info("Quit Done!");
    }

    /**
     * 发送邮件正文
     * @throws IOException
     */
    private void sendEmailContent() throws IOException{

        sendData("DATA");

        int response = getResponse();

        if (response != 354) {
            throw new IOException("Send Email content fail");
        }

        sendData(mail.getContent());
        sendData(".");

        response = getResponse();
        if (response != 250) {
            throw new IOException("Send Email Content fail");
        }

        logger.info("Send Email Content done!");
    }

    /**
     * 发送邮件头，即(mail from, rcpt to以及subject)
     * @throws IOException
     */
    private void sendEmailHeader() throws IOException{

        sendData("MAIL FROM:<" + mail.getFrom() + ">");

        int response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        sendData("RCPT TO:<" + mail.getTo() + ">");

        response = getResponse();

        if (response != 250) {
            throw new IOException("Send Email Header fail!");
        }

        logger.info("Send Email Header done!");
    }

    /**
     * 向邮件服务器发送helo指令
     * @throws IOException
     */
    private void ehlo() throws IOException{
        sendData("HELO " + server);

        int response = getResponse();

        if (response != 250) {
            throw new IOException("ehlo fail!");
        }

        logger.info("ehlo done!");
    }

    /**
     * 发送字符串给邮件服务器
     * @param s 要发送的字符串
     */
    private void sendData(String s) {
        out.println(s);
        out.flush();
    }

    /**
     * 初始化smtp客户端
     */
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

    /**
     * 得到服务器的响应信息
     * @return
     */
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

    /**
     * 关闭smtp客户端
     */
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
        Mail mail = new Mail();
        mail.setFrom("jtdx_159020@sohu.com");
        mail.setTo("465391062@qq.com");
//        mail.setFrom("wangtaoking1@127.0.0.1");
//        mail.setTo("sysuyezhiqi@163.com");
        mail.setSubject("Hello");
        String body = "from:<" + mail.getFrom() + ">" + "\n";
        body += "to:<" + mail.getTo() + ">" + "\n";
        body += "Subject:This is a test email" + "\n";
        body += "\n";
        body += "Hello World!";
        SMTPClient smtpClient = new SMTPClient(mail, Constants.CLIENT_SEND_TO_LOCAL);
        smtpClient.sendEmail();
    }
}

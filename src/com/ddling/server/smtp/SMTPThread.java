package com.ddling.server.smtp;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by ddling on 2014/12/24.
 */
public class SMTPThread implements Runnable {
    private static int     SO_TIMEOUT          = 30000;
    private BufferedReader in                  = null;
    private PrintWriter    out                 = null;
    private int            cureent_server_Type = -1;
    private Socket         clientSocket        = null;
    private SMTPCmdQueue   smtpCmdQueue        = null;
    private MailContent    mailContent         = null;

    public static Logger logger = LoggerFactory.getLogger(SMTPThread.class);

    public SMTPThread(Socket clientSocket, int server_type) {
        this.clientSocket   = clientSocket;
        cureent_server_Type = server_type;
        mailContent = new MailContent();
    }

    /**
     * 输出响应值给客户端
     * @param response 响应字符串
     */
    public void printToClient(String response) {
        out.println(response);
        out.flush();
    }

    public int getCurrentServerType() {
        return cureent_server_Type;
    }

    public MailContent getMailContent() {
        return mailContent;
    }

    public void setMailContent(MailContent mailContent) {
        this.mailContent = mailContent;
    }

    /**
     * 关闭当前客户端连接
     */
    public void close() {
        if (clientSocket != null) {
            printToClient("221 Bye");
            try {
                clientSocket.close();
                logger.info("Client " + clientSocket.getInetAddress() + " closed!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientSocket = null;
        }
    }

    /**
     * 主要流程是：
     * 读取客户端的指令，对客户端中的每一条指令，服务器返回对应的响应值。
     */
    public void run() {
        try {
            clientSocket.setSoTimeout(SO_TIMEOUT);
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printToClient("220 ddling mail system ");

        smtpCmdQueue = new SMTPCmdQueue();

        while (true) {
            try {
                String cmd = in.readLine();
                smtpCmdQueue.process(this, cmd);
                if (clientSocket == null) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

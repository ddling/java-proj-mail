package com.ddling.server.smtp;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

/**
 * Created by ddling on 2014/12/24.
 */
public class SMTPServer implements Runnable {
    private static int   SERVER_TYPE_FOR_SERVER = 0;
    private static int   SERVER_TYPE_FOR_CLIENT = 1;
    private int          port                   = 25;
    private ServerSocket serverSocket           = null;
    private Executor     service                = null;
    private int          cureent_server_Type    = -1;

    public static Logger logger = LoggerFactory.getLogger(SMTPServer.class);

    /**
     * The Default Constructor
     * @param server_type
     * @param port The port What the server socket listen
     */
    public SMTPServer(int server_type, int port) {
        cureent_server_Type = server_type;
        this.port           = port;
    }

    /**
     * start the smtp server and wait for client to connect
     */
    private void startSmtpServer() {
        try {
            serverSocket = new ServerSocket(port);
            service      = Executors.newCachedThreadPool();
            logger.info("SMTP server start at port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client " + clientSocket.getInetAddress() + " Connected to SMTP server");
                service.execute(new SMTPThread(clientSocket, cureent_server_Type));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.startSmtpServer();
        this.stop();
    }

    /**
     * Stop the smtp server
     */
    private void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                logger.info("SMTP server stop at port " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

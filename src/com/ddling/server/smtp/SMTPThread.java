package com.ddling.server.smtp;

import com.ddling.utils.LoggerFactory;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.SocketException;
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

    public static Logger logger = LoggerFactory.getLogger(SMTPThread.class);

    public SMTPThread(Socket clientSocket, int server_type) {
        this.clientSocket   = clientSocket;
        cureent_server_Type = server_type;
    }

    /**
     * Print the response string to client
     * @param response Response String
     */
    public void printToClient(String response) {
        out.println(response);
        out.flush();
    }

    /**
     * Close the current client connection
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

    public void run() {
        try {
            clientSocket.setSoTimeout(SO_TIMEOUT);
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                String line = in.readLine();

                if (clientSocket == null) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

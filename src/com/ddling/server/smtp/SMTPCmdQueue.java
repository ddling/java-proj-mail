package com.ddling.server.smtp;

import com.ddling.server.smtp.State.*;
import com.ddling.utils.Constants;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ddling on 2014/12/24.
 */
public class SMTPCmdQueue {

    private Queue<State> cmdQueue = null;

    public SMTPCmdQueue() {
        cmdQueue = new LinkedList<State>();
        cmdQueue.add(new Helo());
    }

    public void process(SMTPThread smtpThread, String cmd) {
        if (!cmdQueue.isEmpty()) {
            State currentState = cmdQueue.element();

            if (currentState instanceof Helo) {
                if (currentState.process(smtpThread, cmd)) {
                    processHelo(smtpThread);
                }
            }

            if (currentState instanceof Auth) {
                if (currentState.process(smtpThread, cmd)) {
                    processAuth();
                }
            }

            if (currentState instanceof Login) {
                if (currentState.process(smtpThread, cmd)) {
                    processLogin();
                }
            }

            if (currentState instanceof Mail) {
                if (currentState.process(smtpThread, cmd)) {
                    processMail();
                }
            }

            if (currentState instanceof Rcpt) {
                if (currentState.process(smtpThread, cmd)) {
                    processRcpt();
                }
            }
        }
    }

    private void processHelo(SMTPThread smtpThread) {
        int serverType = smtpThread.getCurrentServerType();
        if (serverType == Constants.SERVER_TYPE_FOR_CLIENT) {
            cmdQueue.remove();
            cmdQueue.add(new Auth());
        } else if (serverType == Constants.SERVER_TYPE_FOR_SERVER) {
        }
    }

    private void processAuth() {
        cmdQueue.remove();
        cmdQueue.add(new Login());
    }

    private void processLogin() {
        cmdQueue.remove();
        cmdQueue.add(new Mail());
    }

    private void processMail() {
        cmdQueue.remove();
        cmdQueue.add(new Rcpt());
    }

    private void processRcpt() {
        cmdQueue.remove();
        cmdQueue.add(new Data());
    }
}

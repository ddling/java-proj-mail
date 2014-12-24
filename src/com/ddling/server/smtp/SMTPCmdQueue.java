package com.ddling.server.smtp;

import com.ddling.server.smtp.State.AuthLogin;
import com.ddling.server.smtp.State.Helo;
import com.ddling.server.smtp.State.State;
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
                    int serverType = smtpThread.getCurrentServerType();
                    if (serverType == Constants.SERVER_TYPE_FOR_CLIENT) {
                        cmdQueue.remove();
                        cmdQueue.add(new AuthLogin());
                        System.out.print("add auth login");
                    } else if (serverType == Constants.SERVER_TYPE_FOR_SERVER) {
                    }
                }
            }

            if (currentState instanceof AuthLogin) {
                if (currentState.process(smtpThread, cmd)) {
                    cmdQueue.remove();
                    System.out.print("Auth login success");
                }
            }
        }
    }
}

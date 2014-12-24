package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Login extends State {

    private String[] state = {"username:", "password:"};
    private String currentState;

    public Login() {
        currentState = state[0];
    }

    public boolean process(SMTPThread smtpThread, String str) {
        System.out.println("login ...");
        if (currentState.equals(state[0])) {
            smtpThread.printToClient("334 " + Base64.encodeBase64String("password:".getBytes()));
            currentState = state[1];
        } else if (currentState.equals(state[1])){
            // login
            smtpThread.printToClient("235 Authentication successful");
            return true;
        }
        return false;
    }
}

package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by ddling on 2014/12/24.
 */
public class AuthLogin extends State {

    public boolean process(SMTPThread smtpThread, String str) {
        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        System.out.println("Auth login ... ");

        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (arg == "") {

            return false;
        }

        if (cmd.equals("auth") && !arg.equals("login")) {
            smtpThread.printToClient("504 Unrecognized authentication type");
            return false;
        } else if (cmd.equals("auth") && arg.equals("login")) {
            smtpThread.printToClient("334 " + Base64.decodeBase64("username:"));
            return true;
        } else {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }
    }
}

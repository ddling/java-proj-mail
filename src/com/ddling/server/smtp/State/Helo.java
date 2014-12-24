package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by ddling on 2014/12/24.
 */
public class Helo extends State{

    @Override
    public boolean process(SMTPThread smtpThread, String str) {

        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (arg == "") {
            smtpThread.printToClient("500 Error: bad syntax");
            return false;
        }

        if (!cmd.equals("helo") && !cmd.equals("ehlo")) {
            smtpThread.printToClient("503 Error: send HELO/EHLO first");
            return false;
        } else {
            smtpThread.printToClient("250 OK");
            return true;
        }
    }
}

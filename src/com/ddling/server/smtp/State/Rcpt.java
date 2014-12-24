package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Rcpt extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (arg == "") {
            smtpThread.printToClient("");
            return false;
        }

        smtpThread.printToClient("250 Mail OK");
        return true;
    }
}

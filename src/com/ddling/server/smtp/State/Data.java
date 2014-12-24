package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Data extends State {

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

        if (cmd.equals("data")) {
            smtpThread.printToClient("354 End data with <CR><LF>.<CR><LF>");
            return true;
        }

        return false;
    }
}

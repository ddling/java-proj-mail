package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Data extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        String cmd = this.getCommandStr(str);

        if (!isValidCommand(cmd)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (cmd.equals("data")) {
            smtpThread.printToClient("354 End data with <CR><LF>.<CR><LF>");
            return true;
        }

        return false;
    }
}

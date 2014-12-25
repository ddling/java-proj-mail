package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;
import java.util.regex.*;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Mail extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        if (!isValidCommand(cmd)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        if (arg.equals("")) {
            smtpThread.printToClient("500 Error: bad syntax");
            return false;
        }

        if (validEmailAddress(arg)) {
            smtpThread.getMailContent().setFrom(getMailAddress(arg));
            smtpThread.printToClient("250 Mail OK");
            return true;
        } else {
            smtpThread.printToClient("Not");
            return false;
        }
    }

    private boolean validEmailAddress(String arg) {

        boolean flag =  Pattern.matches("^\\w+:<\\w+@\\w+(\\.\\w+)*>$", arg);

        if (!flag) {
            return false;
        }

        String args[] = arg.split(":");
        if (args[0].equals("from") && isEmailAddress(getMailAddress(arg))) {
            flag = true;
        }
        return flag;
    }

    private boolean isEmailAddress(String email) {
        if (null == email || "".equals(email)) {
            return false;
        }
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private String getMailAddress(String arg) {
        String mailAddress = arg.substring(arg.indexOf("<") + 1, arg.indexOf(">"));
        return mailAddress;
    }
}

package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by ddling on 2014/12/24.
 */
public class Auth extends State {

    public boolean process(SMTPThread smtpThread, String str) {

        // 得到指令的命令字符串以及命令参数
        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        // 判断命令是否合法
        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        // Auth命令需要添加参数login
        if (arg == "") {
            smtpThread.printToClient("");
            return false;
        }

        if (cmd.equals("auth") && !arg.equals("login")) {
            smtpThread.printToClient("504 Unrecognized authentication type");
            return false;
        } else if (cmd.equals("auth") && arg.equals("login")) {
            smtpThread.printToClient("334 " + Base64.encodeBase64String("username:".getBytes()));
            return true;
        } else {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }
    }
}

package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;

/**
 * Created by ddling on 2014/12/24.
 */
public class Helo extends State{

    @Override
    public boolean process(SMTPThread smtpThread, String str) {

        // 得到指令的命令字符串以及命令参数
        String cmd = this.getCommandStr(str);
        String arg = this.getArgumentStr(str);

        // 判断命令是否合法
        if (!isValidCommand(str)) {
            smtpThread.printToClient("502 Error: command not implemented");
            return false;
        }

        // Helo命令需要添加参数
        if (arg == "") {
            smtpThread.printToClient("500 Error: bad syntax");
            return false;
        }

        // 需要先向服务器发送Helo命令
        if (!cmd.equals("helo") && !cmd.equals("ehlo")) {
            smtpThread.printToClient("503 Error: send HELO/EHLO first");
            return false;
        } else {
            smtpThread.printToClient("250 OK");
            return true;
        }
    }
}

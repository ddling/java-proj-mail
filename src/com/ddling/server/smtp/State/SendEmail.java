package com.ddling.server.smtp.State;

import com.ddling.server.smtp.MailManage;
import com.ddling.server.smtp.SMTPThread;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class SendEmail extends State {

    private StringBuffer stringBuffer = new StringBuffer();

    public boolean process(SMTPThread smtpThread, String str) {

        if (".".equals(str)) {
            smtpThread.getMailContent().setMailContent(stringBuffer.toString());
            MailManage.handleEmail(smtpThread.getCurrentServerType(), smtpThread.getMailContent());
            return true;
        }

        stringBuffer.append(str + "\r\n");
        return false;
    }
}

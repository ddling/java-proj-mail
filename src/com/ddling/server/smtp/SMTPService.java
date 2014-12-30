package com.ddling.server.smtp;

import com.ddling.mailmanage.Mail;
import com.ddling.mailmanage.MailManage;
import com.ddling.utils.Constants;

/**
 * Created by lingdongdong on 14/12/30.
 */
public class SMTPService {

    public void handleEmail(SMTPThread smtpThread, Mail mail) {
        if (smtpThread.getCurrentServerType() == Constants.SERVER_TYPE_FOR_CLIENT) {
            MailManage mailManage = new MailManage();
            mailManage.storeEmail(mail);
        }

        if (smtpThread.getCurrentServerType() == Constants.SERVER_TYPE_FOR_SERVER) {
            //ToDo
        }
    }
}

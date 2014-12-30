/*
 * Copyright (C) 2014 lingdongdong
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ddling.mailmanage;

import com.ddling.dbmanage.DBManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lingdongdong on 14/12/29.
 */
public class MailManage {

    /**
     * 得到收件箱中的邮件
     * @param email 隶属于某个邮箱的邮件
     * @return
     */
    public List<Mail> getReceiveEmailInBox(String email) {

        List<Mail> mailList = new LinkedList<Mail>();

        mailList = getEmailList("from", email);

        return mailList;
    }

    /**
     * 得到发件箱中的邮件
     * @param email 隶属于某个邮箱的邮件
     * @return
     */
    public List<Mail> getSendEmailInBox(String email) {

        List<Mail> mailList = new LinkedList<Mail>();

        mailList = getEmailList("to", email);

        return mailList;
    }

    /**
     * 将新的邮件保存到数据库中
     * @param mail 新邮件
     */
    public void storeEmail(Mail mail) {
        insertNewEmail(mail);
    }

    private List<Mail> getEmailList(String fromOrTO, String email) {
        List<Mail> mailList = new LinkedList<Mail>();

        DBManage dbManage = DBManage.getDbManageInstance();

        String sql = String.format(
                "SELECT * FROM EMAIL WHERE '%s' = '%s'",
                fromOrTO, email);

        ResultSet resultSet = dbManage.executeQuery(sql);

        try {
            while (resultSet.next()) {

                Mail mail = new Mail();
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                String subject = resultSet.getString("subject");
                String content = resultSet.getString("content");

                mail.setFrom(from);
                mail.setTo(to);
                mail.setSubject(subject);
                mail.setContent(content);

                mailList.add(mail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mailList;
    }

    private void insertNewEmail(Mail mail) {

        DBManage dbManage = DBManage.getDbManageInstance();

        long date = new Date().getTime();

        String dateStr = date + "";

        String sql = String.format(
                "INSERT INTO EMAIL VALUES (NULL, '%s', '%s', '%s', '%s', '%s')",
                mail.getFrom(), mail.getTo(), mail.getSubject(), mail.getContent(), dateStr);

        dbManage.executeUpdate(sql);

    }

    public static void main(String[] args) {
        MailManage mailManage = new MailManage();
        Mail mail = new Mail("465391062@qq.com", "sdsdsdds@tt.com", "Hello", "sdsdsd");
        mailManage.storeEmail(mail);
    }
}

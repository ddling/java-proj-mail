package com.ddling.client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lingdongdong on 14/12/30.
 */
public class MailFrame {

    private JFrame mailFrame;

    private JLabel backgroundJL;

    public MailFrame() {
        initMailFrame();
    }

    private void initMailFrame() {
        mailFrame = new JFrame();
        Container container = mailFrame.getContentPane();

        mailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mailFrame.setBounds(0, 0, 850, 650);
        mailFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new MailFrame();
    }
}

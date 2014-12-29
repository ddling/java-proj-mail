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

package com.ddling.server.smtp.State;

import com.ddling.server.smtp.SMTPThread;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by lingdongdong on 14/12/25.
 */
public class Login extends State {

    private String[] state = {"username:", "password:"};
    private String currentState;

    public Login() {
        currentState = state[0];
    }

    public boolean process(SMTPThread smtpThread, String str) {
        if (currentState.equals(state[0])) {
            smtpThread.printToClient("334 " + Base64.encodeBase64String("password:".getBytes()));
            currentState = state[1];
        } else if (currentState.equals(state[1])){
            // login
            smtpThread.printToClient("235 Authentication successful");
            return true;
        }
        return false;
    }
}

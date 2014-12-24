import com.ddling.server.smtp.SMTPServer;
import com.ddling.server.smtp.SMTPThread;

public class Main {

    public static void main(String[] args) {
        Thread smtpServer = new Thread(new SMTPServer(1, 1198));

        smtpServer.start();

        try {
            smtpServer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

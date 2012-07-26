package emailer;

import java.io.*;
 

public class SendingEmailDemo {
	public static void main(String[] args) throws IOException {
		EmailNotifier en = new EmailNotifier(args);
		en.sendMail();
    }
}

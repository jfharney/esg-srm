package emailer;

import java.io.*;
 

public class SendingEmailDemo {
	public static void main(String[] args) throws IOException {
		EmailNotifier1 en = new EmailNotifier1(args);
		en.sendMail();
    }
}

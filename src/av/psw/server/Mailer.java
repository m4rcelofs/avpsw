package av.psw.server;

import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;

public class Mailer {

	/**
	 * @param nameEmails
	 *            [name][email]
	 */
	public static void mail(String subject, String message,
			String[]... nameEmails) throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		for (String[] nameEmail : nameEmails) {
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress("eset@av-psw.appspotmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					nameEmail[1], nameEmail[0]));

			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
		}

	}

}

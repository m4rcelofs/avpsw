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
	public void mail(String subject, String message, String[]... nameEmails)
			throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("mr.marcelofs@gmail.com"));

		for (String[] nameEmail : nameEmails)
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					nameEmail[1], nameEmail[0]));

		msg.setSubject(subject);
		msg.setText(message);
		Transport.send(msg);

	}

}

package av.psw.server.avpsw;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import av.psw.server.Mailer;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class CheckPswServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(CheckPswServlet.class
			.getName());

	private String[][] emails = { { "marcelo", "mr.marcelofs@gmail.com" },
			{ "alice", "alice.afs@gmail.com" },
			{ "sandra", "sandra.rfs@gmail.com" } };

	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse res) throws ServletException {

		log.info("Starting to fetch psws");
		long start = Calendar.getInstance().getTimeInMillis();

		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();

		String fetchResponse = null;

		try {

			fetchResponse = new String(fetchService.fetch(
					new URL("http://www.nod327.net")).getContent(),
					Charset.defaultCharset());

			new Mailer().mail("eset", parse(fetchResponse), emails);

		} catch (SocketTimeoutException e) {
			log.info(e.toString() + ", retrying");
			doGet(req, res); // retry
		} catch (Exception e) {
			log.warning(e.toString());
			throw new ServletException(e.getMessage(), e);
		}

		long time = Calendar.getInstance().getTimeInMillis() - start;
		log.info("Fetching of passwords took " + time + "ms");

	}

	private String parse(final String html) {

		// remove início - imagem antes dos psw
		String psws = html
				.substring(
						html.indexOf("<img src=\"http://www.nod327.net/nod32-images/nod327.gif\"></p>") + 61,
						html.length());

		// remove final
		psws = psws.substring(0, psws.indexOf("<div"));

		psws = psws.replace("<p>", "\n").replace("</p>", "\n")
				.replace("<br />", "\n").replace("<br/>", "\n");

		return escapeHtml(psws);
	}

	private String escapeHtml(final String html) {
		if (html == null) {
			return null;
		}
		return html.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;");
	}

}

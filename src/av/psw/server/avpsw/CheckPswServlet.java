package av.psw.server.avpsw;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import av.psw.server.Mailer;
import av.psw.server.avpsw.sources.Source;
import av.psw.server.avpsw.sources.TrialEsetKeys;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class CheckPswServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	log					= Logger.getLogger(CheckPswServlet.class
															.getName());

	private String[][]			emails				= {
			{ "marcelo", "mr.marcelofs@gmail.com" },
			{ "alice", "alice.afs@gmail.com" },
			{ "sandra", "sandra.rfs@gmail.com" }	};

	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse res) throws ServletException {

		log.info("Starting to fetch psws");
		long start = Calendar.getInstance().getTimeInMillis();

		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();

		String fetchResponse = null;

		try {

			Source source = new TrialEsetKeys();

			HTTPRequest request = new HTTPRequest(source.getUrl());
			request.getFetchOptions().doNotFollowRedirects();

			fetchResponse = new String(
					fetchService.fetch(request).getContent(),
					Charset.defaultCharset());

			new Mailer().mail("eset", escapeHtml4(source.parse(fetchResponse)),
					emails);

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

}

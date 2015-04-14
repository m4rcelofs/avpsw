package av.psw.server.avpsw;

import static av.psw.server.Mailer.mail;

import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import av.psw.server.avpsw.sources.PswdStrategy;
import av.psw.server.avpsw.sources.indirect.IndirectStrategy;
import av.psw.server.avpsw.sources.indirect.TrialEsetKeys;
import av.psw.server.avpsw.sources.standard.DefaultStrategy;
import av.psw.server.avpsw.sources.standard.TrialEsetKeysV2;

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

		try {

			PswdStrategy strategy = new DefaultStrategy(new TrialEsetKeysV2());
			mail("eset", strategy.run(), emails);

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

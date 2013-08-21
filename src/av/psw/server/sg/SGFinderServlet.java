package av.psw.server.sg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.apphosting.api.DeadlineExceededException;

import av.psw.server.Mailer;

public class SGFinderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(SGFinderServlet.class
			.getName());

	private Mailer mailer = new Mailer();

	private List<String> tempPrivs = new ArrayList<String>();

	// private char[] letters = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
	// 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
	// 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7',
	// '8', '9', '0' };

	private char[] letters = new char[] { 't', 'h', 'r', 'i', 'n', 'e', 'q',
			'z', 'o' };

	private String mainURL = "http://www.steamgifts.com/giveaway/";

	@Override
	public void doGet(final HttpServletRequest req,
			final HttpServletResponse res) {

		for (char c1 : letters)
			for (char c2 : letters)
				for (char c3 : letters) {
					for (char c4 : letters) {
						PersistenceManager pm = PMF.getNewPersistenceManager();
						TestedCode list = getList("" + c1 + c2 + c3 + c4, pm);
						try {
							for (char c5 : letters) {
								String code = "" + c1 + c2 + c3 + c4 + c5;
								if (!list.getTestedCodes().contains(code)) {
									try {
										check(code);
										list.addTestedCodes(code);
									} catch (Exception e) {
									}
								}
							}

							pm.makePersistent(list);
							pm.flush();
							pm.close();
						} catch (DeadlineExceededException e) {
							pm.makePersistent(list);
							pm.flush();
							pm.close();
							res.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
						}
					}

				}
		PersistenceManager pm = PMF.getNewPersistenceManager();
		PrivateGiveaways pg = getPrivateGiveaways(pm);
		try {
			mail(pg.getPrivateGiveaways());
		} catch (Exception e) {
			log.severe("couldn't send the full list \n"
					+ pg.getPrivateGiveaways().toString());
		}
		pm.close();
	}

	private TestedCode getList(String wxyz, PersistenceManager pm) {
		try {
			return pm.getObjectById(TestedCode.class, wxyz);
		} catch (JDOObjectNotFoundException e) {
			TestedCode list = new TestedCode(wxyz);
			pm.makePersistent(list);
			pm.flush();
			return list;
		} catch (Exception e) {
			log.severe(e.getMessage());
			return null;
		}
	}

	private PrivateGiveaways getPrivateGiveaways(PersistenceManager pm) {
		try {
			return pm.getObjectById(PrivateGiveaways.class, "privateGiveaways");
		} catch (JDOObjectNotFoundException e) {
			PrivateGiveaways pg = new PrivateGiveaways();
			pm.makePersistent(pg);
			pm.flush();
			return pg;
		} catch (Exception e) {
			log.severe(e.getMessage());
			return null;
		}
	}

	private void check(String code) throws Exception {

		try {
			URL url = new URL(mainURL + code);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			{
				conn.setRequestMethod("GET");
				conn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
				// HttpURLConnection.setFollowRedirects(false);
			}

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.severe(mainURL + code + "\n ------- \n"
						+ conn.getResponseMessage());
				return;
			}

			if (read(conn.getInputStream())) {
				System.err.println(mainURL + code);
				{
					PersistenceManager pm = PMF.getNewPersistenceManager();
					PrivateGiveaways pg = getPrivateGiveaways(pm);
					pg.addPrivateGiveaways(mainURL + code);
					pm.flush();
					pm.close();
				}
				tempPrivs.add(mainURL + code);
			}

			if (tempPrivs.size() > 0) {
				mail(tempPrivs);
				tempPrivs.clear();
			}

		} catch (Exception e) {
			log.warning(e.getMessage());
			throw e;
		}

	}

	private boolean read(InputStream inputStream) throws Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		String line;
		boolean priv = false;

		while ((line = reader.readLine()) != null)
			if (line.contains("Invalid giveaway code."))
				break;
			else if (line.contains("<span class=\"private\">Private:</span>")) {
				priv = true;
				break;
			} else if (line.contains("<div class=\"hosted_by\">"))
				break;

		reader.close();

		return priv;
	}

	private void mail(List<String> list) throws Exception {

		StringBuilder message = new StringBuilder();
		for (String address : list)
			message.append(address).append("<br />");

		mailer.mail("steam gifts", message.toString(), new String[] {
				"Marcelo", "mr.marcelofs@gmail.com" });
	}

	public static void main(String[] args) {
		new SGFinderServlet().doGet(null, null);
	}
}

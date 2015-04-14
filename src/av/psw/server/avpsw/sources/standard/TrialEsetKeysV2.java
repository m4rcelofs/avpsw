package av.psw.server.avpsw.sources.standard;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TrialEsetKeysV2 implements DefaultSource {

	@Override
	public URL getUrl() {
		try {
			return new URL("http://trialesetkeys.com/en");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String parse(String html) {
		Document doc = Jsoup.parse(html);

		Element ess = doc
				.select("#table_show_key > table:nth-child(3) > tbody:nth-child(1)")
				.first();

		Elements rows = ess.select("tr");

		StringBuilder text = new StringBuilder();
		for (Element row : rows) {
			if (row.childNodes().size() > 2) {
				String username = row.child(0).text();
				String password = row.child(2).text();
				if (username.startsWith("TRIAL"))
					text.append(username).append(" : ").append(password)
							.append("\n\n");
			}
		}

		return text.toString();
	}

}

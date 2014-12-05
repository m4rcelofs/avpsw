package av.psw.server.avpsw.sources.standard;

import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FreeActivator implements DefaultSource {

	@Override
	public URL getUrl() {
		try {
			return new URL("http://www.freeactivator.com/eset-nod-32-keys.html");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String parse(String html) {
		Document doc = Jsoup.parse(html);

		Element usersEl = doc
				.select("#wsb-element-112348878 > div:nth-child(1) > div:nth-child(6) > span:nth-child(2)")
				.first();

		Element passwordsEl = doc
				.select("#wsb-element-112367178 > div:nth-child(1) > pre:nth-child(5) > span:nth-child(1)")
				.first();

		String[] users = usersEl.html().split("<br>");
		String[] passwords = passwordsEl.html().split("<br>");

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < users.length; i++)
			if (users[i].startsWith("TRIAL"))
				builder.append(users[i]).append(" : ").append(passwords[i])
						.append("\n\n");

		return builder.toString();
	}

}

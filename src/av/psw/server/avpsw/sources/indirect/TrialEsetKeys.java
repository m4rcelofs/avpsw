package av.psw.server.avpsw.sources.indirect;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TrialEsetKeys implements IndirectSource {

	@Override
	public URL getUrl() {
		return toURL("http://trialesetkeys.com/");
	}

	@Override
	public URL[] parseIndirectUrls(String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("head > script");
		List<URL> srcs = new ArrayList<>();
		for (Element e : elements) {
			String src = e.attr("src");
			if (src != null && !src.isEmpty())
				srcs.add(toURL(src));
		}
		return srcs.toArray(new URL[srcs.size()]);
	}

	@Override
	public String parse(String js) {
		int tableBegins = js.indexOf("<table id=\"table_4_key\">");
		int tableEnds = js.indexOf("</table>';", tableBegins) + 8;
		String html = js.substring(tableBegins, tableEnds);

		Document doc = Jsoup.parse(html);
		Elements rows = doc.select("tr");

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

	private URL toURL(String s) {
		try {
			return new URL(s);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

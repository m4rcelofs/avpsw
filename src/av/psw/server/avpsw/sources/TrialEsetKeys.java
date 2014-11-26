package av.psw.server.avpsw.sources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TrialEsetKeys implements Source {

	@Override
	public URL getUrl() {
		try {
			return new URL(
					"http://trialesetkeys.com/sites/default/files/js/js_V7UEunRb7q8Vb7Uj7ZbiCZUZn86j3VQp-BuE9WbikEs.js");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String parse(String js) {
		int tableBegins = js.indexOf("<table id=\"table_4_key\">");
		int tableEnds = js.indexOf("</table>", tableBegins) + 8;
		String html = js.substring(tableBegins, tableEnds);

		Document doc = Jsoup.parse(html);
		Elements rows = doc.select("tr");

		StringBuilder text = new StringBuilder();
		for (Element row : rows) {
			if (row.childNodes().size() > 2) {
				String username = row.child(0).text();
				String password = row.child(2).text();
				text.append(username).append(" : ").append(password)
						.append("\n\n");
			}
		}

		return text.toString();
	}

	public static void main(String[] args) throws IOException {
		Source source = new TrialEsetKeys();
		String js = IOUtils.toString(source.getUrl());
		String html = source.parse(js);
		System.out.println(html);
	}
}

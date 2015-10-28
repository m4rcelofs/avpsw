package av.psw.server.avpsw.sources.standard;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EsetNod32AntivirusTrialKey implements DefaultSource {

	@Override
	public URL getUrl() {
		try {
			return new URL("http://esetnod32antivirustrialkey.blogspot.com.br/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String parse(String html) {
		Document doc = Jsoup.parse(html);

		String post = doc
				.select(".post-body.entry-content>div")
				.first()
				.text();

		return post
				.replaceAll("User Name.{1}", "\n\nUsername:")
				.replaceAll("Password.{1}", "\nPassword:")
				.replaceAll("Valid until.{1}", "\nValid until: ")
				.replaceAll("For more keys.+", "");
	}
	
	public static void main(String[] args) throws Exception {
		String html = IOUtils.toString(new URL("http://esetnod32antivirustrialkey.blogspot.com.br/"));
		String parsed = new EsetNod32AntivirusTrialKey().parse(html);
		System.out.println(parsed);
	}
}
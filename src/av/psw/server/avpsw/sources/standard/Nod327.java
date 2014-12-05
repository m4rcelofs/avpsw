package av.psw.server.avpsw.sources.standard;

import java.net.MalformedURLException;
import java.net.URL;

public class Nod327 implements DefaultSource {

	@Override
	public URL getUrl() {
		try {
			return new URL("http://www.nod327.net");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String parse(String html) {
		// remove início - imagem antes dos psw
		String psws = html
				.substring(
						html.indexOf("<img src=\"http://www.nod327.net/nod32-images/nod327.gif\"></p>") + 61,
						html.length());

		// remove final
		psws = psws.substring(0, psws.indexOf("<div"));

		psws = psws.replace("<p>", "\n").replace("</p>", "\n")
				.replace("<br />", "\n").replace("<br/>", "\n");

		return psws;
	}
}

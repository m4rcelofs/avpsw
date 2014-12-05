package av.psw.server.avpsw.sources.standard;

import java.net.URL;

public interface DefaultSource {

	public URL getUrl();

	public String parse(String html);

}

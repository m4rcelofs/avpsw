package av.psw.server.avpsw.sources;

import java.net.URL;

public interface Source {

	public URL getUrl();
	
	public String parse(String html);
	
}

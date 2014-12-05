package av.psw.server.avpsw.sources.indirect;

import java.net.URL;

public interface IndirectSource {

	public URL getUrl();
	
	public URL[] parseIndirectUrls(String html);
	
	public String parse(String html);
	
}

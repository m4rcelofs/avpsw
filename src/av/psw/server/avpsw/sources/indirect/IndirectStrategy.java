package av.psw.server.avpsw.sources.indirect;

import static av.psw.server.Downloader.download;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.net.URL;

import av.psw.server.avpsw.sources.PswdStrategy;

public class IndirectStrategy implements PswdStrategy {

	private final IndirectSource source;

	public IndirectStrategy(IndirectSource source) {
		this.source = source;
	}

	@Override
	public String run() throws IOException {

		String fetchResponse = download(source.getUrl());
		URL[] indirectLinks = source.parseIndirectUrls(fetchResponse);
		for (URL url : indirectLinks) {
			try {
				String html = download(url);
				String passwords = source.parse(html);
				return escapeHtml4(passwords);
			} catch (Exception e) {
				// wrong file
			}
		}
		throw new IllegalStateException("Could not find proper source file");
	}

}

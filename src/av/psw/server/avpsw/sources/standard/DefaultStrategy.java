package av.psw.server.avpsw.sources.standard;

import static av.psw.server.Downloader.download;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;

import av.psw.server.avpsw.sources.PswdStrategy;

public class DefaultStrategy implements PswdStrategy {

	private final DefaultSource source;

	public DefaultStrategy(DefaultSource source) {
		this.source = source;
	}

	@Override
	public String run() throws IOException {

		String fetchResponse = download(source.getUrl());
		return escapeHtml4(source.parse(fetchResponse));

	}
}

package av.psw.server;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class Downloader {

	private static URLFetchService fetchService = URLFetchServiceFactory
			.getURLFetchService();

	public static String download(URL url) throws IOException {

		String fetchResponse = null;

		HTTPRequest request = new HTTPRequest(url);
		request.getFetchOptions().doNotFollowRedirects();

		fetchResponse = new String(fetchService.fetch(request).getContent(),
				Charset.defaultCharset());

		return fetchResponse;

	}

}

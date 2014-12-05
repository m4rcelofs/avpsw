package av.psw.server.avpsw.sources;

import java.io.IOException;

public interface PswdStrategy {

	public String run() throws IOException;
}

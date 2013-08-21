package av.psw.server.sg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class TestedCode {

	// 3 first letters
	@PrimaryKey
	private String id;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<String> testedCodes;

	public TestedCode(String wxyz) {
		this.id = wxyz;
	}

	public Set<String> getTestedCodes() {
		if (testedCodes == null)
			testedCodes = new HashSet<String>();

		return Collections.unmodifiableSet(testedCodes);
	}

	public void addTestedCodes(String testedCode) {
		if (testedCodes == null)
			testedCodes = new HashSet<String>();

		this.testedCodes.add(testedCode);
	}
}

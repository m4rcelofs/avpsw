package av.psw.server.sg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PrivateGiveaways {

	@PrimaryKey
	private String id = "privateGiveaways";

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private List<String> privateGiveaways;

	public List<String> getPrivateGiveaways() {
		if (privateGiveaways == null)
			privateGiveaways = new ArrayList<String>();

		return Collections.unmodifiableList(privateGiveaways);
	}

	public void addPrivateGiveaways(String privateGiveaway) {
		if (privateGiveaways == null)
			privateGiveaways = new ArrayList<String>();

		privateGiveaways.add(privateGiveaway);
	}

}

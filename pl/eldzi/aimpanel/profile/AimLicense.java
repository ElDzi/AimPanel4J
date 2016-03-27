package pl.eldzi.aimpanel.profile;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Request;
import com.goebl.david.Webb;

import pl.eldzi.aimpanel.utils.DateUtils;

public class AimLicense {
	private String ur;
	private JSONObject obj;

	private String authToken;

	public boolean valid;
	public String notes, name;
	public Date to;

	public AimLicense(String url) {
		if (url.endsWith("/"))
			ur = url + "api/v1/license";
		else
			ur = url + "/api/v1/license";

	}

	public AimLicense withAuth(String authToken) {
		this.authToken = authToken;
		return this;
	}

	public AimLicense send() {
		Webb w = Webb.create();
		Request r = w.get(ur).header("Authorization", authToken).ensureSuccess();
		obj = r.asJsonObject().getBody();
		try {
			valid = obj.getBoolean("valid");

			notes = obj.getString("notes");
			to = DateUtils.parse(obj.getString("valid_to"));
			name = obj.getString("name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return this;
	}

	public boolean isValid() {
		return valid;
	}

	public String getNotes() {
		return notes;
	}

	public String getName() {
		return name;
	}

	public Date getTo() {
		return to;
	}

	public JSONObject getResult() {
		return obj;
	}

}

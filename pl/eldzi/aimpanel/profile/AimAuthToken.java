package pl.eldzi.aimpanel.profile;

import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Response;
import com.goebl.david.Webb;

public class AimAuthToken {
	private String ur;
	private JSONObject obj;

	public boolean isError;
	public String authToken;

	public AimAuthToken(String url) {
		if (url.endsWith("/"))
			ur = url + "api/v1/auth/login";
		else
			ur = url + "/api/v1/auth/login";

	}

	private String username, password;

	public AimAuthToken withUsername(String u) {
		username = u;
		return this;
	}

	public AimAuthToken withPassword(String p) {
		password = p;
		return this;
	}

	public AimAuthToken send() throws JSONException {
		Webb w = Webb.create();
		obj = w.post(ur).param("username", username).param("password", password).ensureSuccess().asJsonObject()
				.getBody();
		isError = obj.getBoolean("error");
		authToken = obj.getString("token");
		System.out.println("ERR: " + isError);
		System.out.println("TOK: " + authToken);
		return this;
	}

	public boolean isError() {
		return isError;
	}

	public String getAuthToken() {
		return authToken;
	}
}

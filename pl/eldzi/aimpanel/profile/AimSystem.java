package pl.eldzi.aimpanel.profile;

import org.json.JSONObject;

import com.goebl.david.Request;
import com.goebl.david.Webb;

import pl.eldzi.aimpanel.Main;

public class AimSystem {
	private String ur;
	private JSONObject obj;

	public String authToken;

	public AimSystem(String url) {
		if (url.endsWith("/"))
			ur = url + "api/v1/os/stats";
		else
			ur = url + "/api/v1/os/stats";

	}

	public AimSystem withAuth(String authToken) {
		this.authToken = authToken;
		return this;
	}

	public AimSystem send() {
		Webb w = Webb.create();
		Request r = w.get(ur).header("Authorization", authToken).ensureSuccess();
		obj = r.asJsonObject().getBody();
		System.out.println(obj.toString());
		return this;
	}

	public JSONObject getResult() {
		return obj;
	}

	public static void main(String[] args) {
		new AimSystem("http://demo.aimpanel.pro:3131/").withAuth(Main.AUTH).send();

	}
}

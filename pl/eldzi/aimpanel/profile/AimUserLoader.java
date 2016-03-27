package pl.eldzi.aimpanel.profile;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

import pl.eldzi.aimpanel.utils.DateUtils;

public class AimUserLoader {
	public static AimUser getByUsername(String username, String url) {
		try {
			String auth = new AimAuthToken(url).withUsername("admin").withPassword("cAoRdYpeZssWxGKU").send()
					.getAuthToken();
			Webb w = Webb.create();
			JSONObject o = w.get(url + "/api/v1/user/" + username + "/info").header("Authorization", auth)
					.ensureSuccess().asJsonObject().getBody();
			boolean ex = o.getBoolean("exists");
			Date d = DateUtils.parse(o.getString("created_at"));
			String u = o.getString("username");
			AimUser us = new AimUser(u, d, ex);
			return us;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		throw new NullPointerException("User dont found!");

	}

	public static ArrayList<AimUser> getUsers(String url, String auth) {
		Webb w = Webb.create();
		JSONArray u = w.get(url + "/api/v1/user/").header("Authorization", auth).ensureSuccess().asJsonArray()
				.getBody();
		ArrayList<AimUser> profiles = new ArrayList<>();
		for (int k = 0; k < u.length(); k++) {

			try {
				JSONObject o = u.getJSONObject(k);
				AimUser us = new AimUser(o.getString("username"), DateUtils.parse(o.getString("created_at")), true);
				profiles.add(us);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return profiles;

	}

	public static boolean createUser(String url, String auth, String username, String password) {
		Webb w = Webb.create();
		JSONObject o = w.post(url + "/api/v1/user").header("Authorization", auth).param("username", username)
				.param("password", password).ensureSuccess().asJsonObject().getBody();
		try {
			if (o.getString("status").equalsIgnoreCase("ok")) {
				AimUser au = new AimUser(username, new Date(System.currentTimeMillis()), true);
				return true;
			}
		} catch (JSONException e) {
			return false;
		}
		return false;
	}

}

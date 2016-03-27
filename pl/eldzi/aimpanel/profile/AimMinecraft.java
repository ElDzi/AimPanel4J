package pl.eldzi.aimpanel.profile;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

import pl.eldzi.aimpanel.utils.DateUtils;

public class AimMinecraft {
	private String u, a;
	private static HashMap<Integer, AimMinecraftServer> servers = new HashMap<>();

	public AimMinecraft(String url, String auth) {
		u = url;
		a = auth;
	}

	public HashMap<Integer, AimMinecraftServer> getServers() {
		if (servers.isEmpty())
			loadServers();
		return servers;
	}

	public void createServer(int ID) {
		Webb w = Webb.create();
		JSONObject o = w.post(u + "/api/v1/services/mc/").header("Authorization", a).param("id", ID).ensureSuccess()
				.asJsonObject().getBody();
		System.out.println(o.toString());

	}

	public void loadServers() {
		servers.clear();
		Webb w = Webb.create();
		JSONArray a = w.get(u + "/api/v1/services/mc").header("Authorization", this.a).ensureSuccess().asJsonArray()
				.getBody();
		for (int k = 0; k < a.length(); k++) {
			try {
				JSONObject o = a.getJSONObject(k);
				AimMinecraftServer se = new AimMinecraftServer(u, this.a, o.getBoolean("crashed"),
						DateUtils.parse(o.getString("created_at")), o.getInt("id"), o.getString("state"));
				servers.put(se.getID(), se);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

}

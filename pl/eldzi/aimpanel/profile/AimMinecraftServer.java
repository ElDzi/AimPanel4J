package pl.eldzi.aimpanel.profile;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goebl.david.Webb;

import pl.eldzi.aimpanel.utils.DateUtils;

public class AimMinecraftServer {
	private int spid, pid, ram, upt;
	private float cpu_per;
	private String sess, state;
	private boolean cr;
	private Date s, c;
	private int ID;
	public String url, auth;

	public AimMinecraftServer(String url, String auth, boolean crashed, Date created, int id, String state) {
		cr = crashed;
		c = created;
		this.state = state;
		this.url = url;
		this.auth = auth;
		ID = id;
		try {
			update();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getID() {
		return ID;
	}

	public String startServer() {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/start").header("Authorization", auth).ensureSuccess()
				.asJsonObject().getBody();
		try {
			return o.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String restartServer() {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/restart").header("Authorization", auth)
				.ensureSuccess().asJsonObject().getBody();
		try {
			return o.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public boolean deleteServer() throws JSONException {
		Webb w = Webb.create();
		JSONObject o = w.delete(url + "/api/v1/services/mc/" + ID).header("Authorization", auth).ensureSuccess()
				.asJsonObject().getBody();
		if (o.getString("status").equals("ok"))
			return true;
		else
			return false;

	}

	public String stopServer() {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/stop").header("Authorization", auth).ensureSuccess()
				.asJsonObject().getBody();
		try {
			return o.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String[] getLastLinesFromLog() {
		Webb w = Webb.create();
		JSONArray o = w.get(url + "/api/v1/services/mc/" + ID + "/log").header("Authorization", auth).ensureSuccess()
				.asJsonArray().getBody();

		// REMOVING HTML and another shits
		String f = o.toString();
		f = f.replaceAll("\\<[^>]*>", "");
		f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "")
				.replace("type &quot;help&quot; or &quot;?&quot;", "");
		String[] kl = f.split(",");
		return kl;
	}

	public String[] getLastLinesFromConsole() {
		Webb w = Webb.create();
		JSONArray o = w.get(url + "/api/v1/services/mc/" + ID + "/console").header("Authorization", auth)
				.ensureSuccess().asJsonArray().getBody();

		// REMOVING HTML and another shits
		String f = o.toString();
		f = f.replaceAll("\\<[^>]*>", "");
		f = f.replace("[\"", "").replace("\"", "").replace("\\tat", "")
				.replace("type &quot;help&quot; or &quot;?&quot;", "");
		String[] kl = f.split(",");
		for (String a : kl) {
			System.out.println(a);
		}
		return kl;
	}

	public String[] getLastLinesFromConsole(int range) {
		String[] f = getLastLinesFromConsole();
		String[] d = new String[range];
		for (int k = f.length - 1; k > (k - range); k--) {
			range--;
			d[range] = f[k];
		}

		return d;
	}

	public String[] getLastLinesFromLog(int range) {
		String[] f = getLastLinesFromLog();
		String[] d = new String[range];
		for (int k = f.length - 1; k > (k - range); k--) {
			range--;
			d[range] = f[k];
		}

		return d;
	}

	public boolean sendCommand(String command) throws JSONException {
		Webb w = Webb.create();
		JSONObject o = w.post(url + "/api/v1/services/mc/" + ID + "/cmd").header("Authorization", auth)
				.param("cmd", command).ensureSuccess().asJsonObject().getBody();
		if (o.getString("status").equals("ok"))
			return true;
		else
			return false;
	}

	public String getRandomPasswordForSFTP() {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/password/random").header("Authorization", auth)
				.ensureSuccess().asJsonObject().getBody();
		try {
			return o.getString("new_password");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getServerProperties() throws JSONException {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/server.properties").header("Authorization", auth)
				.ensureSuccess().asJsonObject().getBody();
		HashMap<String, Object> outMap = new HashMap<>();
		Iterator<String> nameItr = o.keys();
		while (nameItr.hasNext()) {
			String k = nameItr.next();
			Object v = o.get(k);
			outMap.put(k, v);
		}
		return outMap;
	}

	public Date getStartDate() {
		return s;
	}

	public int getRamKb() {
		return ram;
	}

	public int getUptime() {
		return upt;
	}

	public String getState() {
		return state;
	}

	public Date getCreateDate() {
		return c;
	}

	public float getCpuUsagePercentage() {
		return cpu_per;
	}

	public boolean isCrashed() {
		return cr;
	}

	public int getScreenSessionPID() {
		return spid;
	}

	public int getPID() {
		return pid;
	}

	public String getSessionName() {
		return sess;
	}

	public void update() throws JSONException {
		Webb w = Webb.create();
		JSONObject o = w.get(url + "/api/v1/services/mc/" + ID + "/info").header("Authorization", auth).ensureSuccess()
				.asJsonObject().getBody();
		if (!o.isNull("session"))
			sess = o.getString("session");
		if (!o.isNull("screen_pid"))
			spid = o.getInt("screen_pid");
		if (!o.isNull("pid"))
			pid = o.getInt("pid");
		if (!o.isNull("cpu"))
			cpu_per = o.getInt("cpu");
		if (!o.isNull("ram_kb"))
			ram = o.getInt("ram_kb");
		if (!o.isNull("crashed"))
			cr = o.getBoolean("crashed");
		if (!o.isNull("started_at"))
			s = DateUtils.parse(o.getString("started_at"));
		if (!o.isNull("created_at"))
			c = DateUtils.parse(o.getString("created_at"));
		if (!o.isNull("state"))
			state = o.getString("state");
		if (!o.isNull("uptime_sec"))
			upt = o.getInt("uptime_sec");

	}

}

package pl.eldzi.aimpanel.profile;

import java.util.Date;

import com.sun.xml.internal.fastinfoset.algorithm.UUIDEncodingAlgorithm;

public class AimUser {
	// {"exists":true,"created_at":"2016-02-11T02:19:15+00:00","username":"admin"}

	private String u;
	private Date d;
	private boolean e;

	public AimUser(String username, Date created_at, boolean exists) {
		u = username;
		d = created_at;
		e = exists;
	}
}

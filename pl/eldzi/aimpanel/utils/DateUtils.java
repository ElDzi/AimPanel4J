package pl.eldzi.aimpanel.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static Date parse(String stringData) {
		final DateFormat fmt;
		if (stringData.endsWith("Z")) {
			fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		} else {
			fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		}
		try {
			return fmt.parse(stringData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}

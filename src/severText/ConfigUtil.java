package severText;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import severText.Constants;

public class ConfigUtil {
	static Properties ps = new Properties();
	static {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					Constants.CFG_DIR + File.separator + "mysql.properties"));
			ps.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperValue(String key) {
		return ps.getProperty(key);
	}
}

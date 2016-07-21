package com.sldt.mds.dmc.mp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	//配置文件
	private static final String PROPERTIES = "conf.properties";
	public static Properties props = null;
	
	static {
		PropertiesUtil u = new PropertiesUtil();
		u.init();
	}

	public void init() {
		InputStream in = null;
		props = new Properties();
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES);
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

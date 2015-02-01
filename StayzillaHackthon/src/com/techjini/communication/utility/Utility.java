/**
 * 
 */
package com.techjini.communication.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * @author Abhimaan Madhav
 * 
 */
public class Utility {

	private volatile static Properties properties;

	public Properties loadPropties(Context context, String fileName)
			throws IOException {
		if (null != properties) {
			return properties;
		}
		properties = new Properties();
		String file = fileName;
		try {
			InputStream fileStream = context.getAssets().open(file);
			properties.load(fileStream);
			fileStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return properties;
	}

}

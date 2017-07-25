package com.datalicious.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ParamExtractionUtility {

	public static HashMap<String, String> getQueryMap(String queryString) {

		HashMap<String, String> map = new HashMap<String, String>();

		try {

			if (queryString == null || queryString.equals("")) {
				return map;
			}

			String[] params = queryString.split("&");

			for (String param : params) {

				String[] arr = param.split("=");
				String name = URLDecoder.decode(arr[0], StandardCharsets.UTF_8.name());
				;
				String value = arr.length > 1 ? URLDecoder.decode(arr[1], StandardCharsets.UTF_8.name()) : "";

				map.put(name, value);
			}

		} catch (Exception e) {
			System.err.println("Error in getQueryMap() method !!" + e.getMessage());
		}

		return map;
	}

}

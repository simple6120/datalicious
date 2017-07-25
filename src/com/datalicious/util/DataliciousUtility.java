/**
 * 
 */
package com.datalicious.util;

;

public class DataliciousUtility {

	private static final String KEY = "webdriver.chrome.driver";
	private static final String VALUE = "C:\\Program Files (x86)\\Google\\chromedriver.exe";
	private static final String SEARCH_KEYWORD = "Datalicious";

	public static void main(String[] args) {

		System.setProperty(KEY, VALUE);

		String url = SearchUtility.getURLFromGoogleSearch(SEARCH_KEYWORD);

		NetworkLogging.checkAllRequestOnCurrentPage(url);

	}

}

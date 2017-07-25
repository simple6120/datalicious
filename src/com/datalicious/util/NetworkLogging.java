/**
 * 
 */
package com.datalicious.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author tiwariar
 *
 */
public class NetworkLogging {

	private static final String URL = "url";
	private static final String PARAMS = "params";
	private static final String METHOD = "method";
	private static final String MESSAGE = "message";
	private static final String REQUEST = "request";
	private static final String OPTIMAHUB = "https://dc.optimahub.com";
	private static final String PERFORMANCE = "performance";
	private static final String REQUEST_SEND = "Network.requestWillBeSent";
	private static final String GOOGLE_ANALYTICS = "https://www.google-analytics.com/r/collect";
	private static final String CSV_FILE_PATH = "C:\\Users\\tiwariar\\workspace\\Datalicious\\resource\\log.csv";
	private static boolean APPEND = false;

	protected static void checkAllRequestOnCurrentPage(String url) {

		ChromeDriver driver = null;

		try {

			ChromeOptions options = new ChromeOptions();

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);

			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

			driver = new ChromeDriver(cap);

			driver.navigate().to(url);

			LogEntries logs = driver.manage().logs().get(PERFORMANCE);

			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {

				LogEntry logEntry = it.next();

				checkSpecificRequestWasMade(logEntry.getMessage());

			}

		} catch (Exception e) {
			System.err.println("Error in checkAllRequestOnCurrentPage() method !!" + e.getMessage());
		} finally {

			if (driver != null) {
				driver.quit();
			}
		}

	}

	private static void checkSpecificRequestWasMade(String logEntryStr) {

		try {

			if (logEntryStr != null && !logEntryStr.equals("")) {

				if (logEntryStr.contains(REQUEST_SEND)) {

					if (logEntryStr.contains(GOOGLE_ANALYTICS) || logEntryStr.contains(OPTIMAHUB)) {

						JsonParser parser = new JsonParser();

						JsonObject logJson = parser.parse(logEntryStr).getAsJsonObject();

						JsonObject message = logJson.getAsJsonObject(MESSAGE);

						String method = message.get(METHOD).isJsonNull() ? "" : message.get(METHOD).getAsString();

						if (REQUEST_SEND.equals(method)) {

							JsonObject params = message.getAsJsonObject(PARAMS);

							if (params != null) {

								JsonObject request = params.getAsJsonObject(REQUEST);

								createCSVFile(request.get(URL).isJsonNull() ? "" : request.get(URL).getAsString());
							}

						}

					}
				}
			}

		} catch (Exception e) {
			System.err.println("Error in checkSpecificRequestWasMade() method !!" + e.getMessage());
		}

	}

	private static void createCSVFile(String currentURL) {
		FileWriter writer = null;

		try {

			if (currentURL.contains(GOOGLE_ANALYTICS)) {

				writer = new FileWriter(CSV_FILE_PATH, APPEND);

				writer.append("\n");
				writer.append(GOOGLE_ANALYTICS + ": Yes");

				currentURL = currentURL.replaceAll(GOOGLE_ANALYTICS + "(.*)?[?]", "");

				HashMap<String, String> queryParam = ParamExtractionUtility.getQueryMap(currentURL);

				if (queryParam != null && !queryParam.isEmpty()) {

					writer.append("\n");

					if (queryParam.containsKey("dt")) {
						writer.append("dt: " + queryParam.get("dt"));
						writer.append(",");
					}

					if (queryParam.containsKey("dp")) {
						writer.append("dp: " + queryParam.get("dp"));
						writer.append(",");
					}
				}

			} else if (currentURL.contains(OPTIMAHUB)) {

				writer = new FileWriter(CSV_FILE_PATH, APPEND);

				writer.append("\n");
				writer.append(OPTIMAHUB + ": Yes");

			}

			if (!APPEND) {
				APPEND = true;
			}

		} catch (Exception e) {
			System.err.println("Error in createCSVFile method" + e.getMessage());
		} finally {

			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

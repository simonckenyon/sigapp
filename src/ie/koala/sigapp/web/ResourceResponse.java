/**
 * 
 */
package ie.koala.sigapp.web;

import info.bliki.wiki.model.WikiModel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.res.AssetManager;
import android.util.Log;
import android.webkit.WebResourceResponse;

/**
 * @author simon
 * 
 */
public class ResourceResponse {

	private final static String TAG = ResourceResponse.class.getSimpleName();

	public static WebResourceResponse response(String url, AssetManager am) {
		Log.d(TAG, "shouldInterceptRequest(): fileName=" + url);
		String mimeType;

		String fileName = getFileName(url);
		Log.d(TAG, "fileName=" + fileName);

		mimeType = getMimeType(fileName);
		Log.d(TAG, "mimeType=" + mimeType);

		if (fileName.endsWith("wiki")) {
			return textResponse(fileName, mimeType, am);
		} else {
			return binaryResponse(fileName, mimeType, am);
		}
	}

	public static String getFileName(String url) {
		// remove "assets://" from beginning of url
		return url.substring(9);
	}

	public static String getMimeType(String fileName) {
		if (fileName.endsWith("png")) {
			return "image/png";
		} else if (fileName.endsWith("jpg")) {
			return "image/jpeg";
		} else if (fileName.endsWith("html")) {
			return "text/html";
		} else if (fileName.endsWith("wiki")) {
			return "text/html";
		} else if (fileName.endsWith("js")) {
			return "text/javascript";
		} else if (fileName.endsWith("css")) {
			return "text/css";
		} else {
			return "";
		}
	}

	private static WebResourceResponse textResponse(String fileName,
			String mimeType, AssetManager am) {
		InputStream in_s;
		InputStream out_s;
		try {
			in_s = am.open(fileName);
			String encoding = "UTF-8";
			WikiModel wikiModel = new WikiModel(
					"assets://app/images/${image}",
					"assets://app/web/${title}.wiki");
			String wikiStr = getStringFromInputStream(in_s);
//			Log.d(TAG, "wikiStr=" + wikiStr);
			String htmlStr = wikiModel.render(wikiStr);
//			Log.d(TAG, "htmlStr=" + htmlStr);
			out_s = new ByteArrayInputStream(htmlStr.getBytes(encoding));
			WebResourceResponse response = new WebResourceResponse(mimeType,
					encoding, out_s);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	private static WebResourceResponse binaryResponse(String fileName,
			String mimeType, AssetManager am) {
		InputStream in_s;
		try {
			in_s = am.open(fileName);
			String encoding = "UTF-8";
			WebResourceResponse response = new WebResourceResponse(mimeType,
					encoding, in_s);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

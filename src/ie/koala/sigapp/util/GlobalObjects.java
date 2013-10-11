/**
 * 
 */
package ie.koala.sigapp.util;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import ie.koala.sigapp.simonkenyon.R;
import ie.koala.sigapp.xml.App;

/**
 * @author simon
 * 
 */
public class GlobalObjects extends Application {
	protected static final String TAG = GlobalObjects.class.getSimpleName();

	private static App app = null;

	private static String versionName = "";
	private static String versionCode = "";
	private static String versionBuildTimestamp = "";
	private static String versionGitHash = "";

	private static String sharingMessage = "";
	private static String whatToShareTitle = "";
	private static String whatToShare = "";
	private static boolean analyticsEnabled = true;

	public static OnSharedPreferenceChangeListener getPreferences(
			final Context context) {
		OnSharedPreferenceChangeListener listener;

		// make sure that preferences are initialised to their default values on
		// first run
		PreferenceManager.setDefaultValues(context, R.xml.pref_general, false);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);

		// get the calling class name
		final String name = context.getClass().getSimpleName();

		// Use instance field for listener
		// It will not be gc'd as long as this instance is kept referenced
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences settings,
					String key) {
				Log.d(TAG, "onSharedPreferenceChanged called by " + name);
				((Activity) context).invalidateOptionsMenu();
				if (key.equals("sharing_message")) {
					sharingMessage = settings.getString(key, null);
					Log.i(TAG, "callback sharing_message=" + sharingMessage);
				} else if (key.equals("what_to_share")) {
					String[] titles = context.getResources().getStringArray(R.array.pref_sharing_list_titles);
					String[] values = context.getResources().getStringArray(R.array.pref_sharing_list_values);
					whatToShare = settings.getString(key, null);					
					for (int i = 0; i < values.length; i++) {
						if (values[i].equals(whatToShare)) {
							// found setting
							whatToShareTitle = titles[i];
						}
					}
					Log.i(TAG, "callback what_to_share title=\"" + whatToShareTitle + "\" whatToShare=\""+ whatToShare + "\"");
				} else if (key.equals("google_analytics")) {
					analyticsEnabled = settings.getBoolean(key, false);
					if (analyticsEnabled) {
						// Google analytics enable
						GoogleAnalytics.getInstance(context)
								.setAppOptOut(false);
						Log.i(TAG, "Google analytics is enabled");
					} else {
						// Google analytics
						// Before disabling analytics lets record that the user
						// decided to disable analytics
						EasyTracker.getInstance(context).send(
								MapBuilder.createEvent("ui_action", // Event
																	// category
										// (required)
										"button_press", // Event action
														// (required)
										"User opt-out of google analytics", // Event
																			// label
										null) // Event value
										.build());
						Log.e(TAG, "User opt-out of google analytics, null");

						// Google analytics disable

						GoogleAnalytics.getInstance(context).setAppOptOut(true);
						Log.i(TAG, "Google analytics is disabled");
					}
					Log.i(TAG, "callback google_analytics=" + analyticsEnabled);
				} else {
					Log.i(TAG, "in global preferences listener key=\"" + key
							+ "\"");
				}
			}
		};
		settings.registerOnSharedPreferenceChangeListener(listener);

		sharingMessage = settings.getString("sharing_message", null);
		Log.i(TAG, "getPreferences() sharing_message=" +  sharingMessage);

		String[] titles = context.getResources().getStringArray(R.array.pref_sharing_list_titles);
		String[] values = context.getResources().getStringArray(R.array.pref_sharing_list_values);
		whatToShare = settings.getString("what_to_share", null);
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(whatToShare)) {
				// found setting
				whatToShareTitle = titles[i];
			}
		}
		Log.i(TAG, "getPreferences() what_to_share title=\"" + whatToShareTitle + "\" whatToShare=\""+ whatToShare + "\"");

		 analyticsEnabled = settings.getBoolean("google_analytics", false);
		 Log.i(TAG, "getPreferences() google_analytics=" + analyticsEnabled);

		return listener;
	}

	/**
	 * @return the versionName
	 */
	public static String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public static void setVersionName(String name) {
		versionName = name;
	}

	/**
	 * @return the versionCode
	 */
	public static String getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode
	 *            the versionCode to set
	 */
	public static void setVersionCode(String code) {
		versionCode = code;
	}

	/**
	 * @return the buildTimestamp
	 */
	public static String getVersionBuildTimestamp() {
		return versionBuildTimestamp;
	}

	/**
	 * @param buildTimestamp
	 *            the buildTimestamp to set
	 */
	public static void setVersionBuildTimestamp(String timestamp) {
		versionBuildTimestamp = timestamp;
	}

	/**
	 * @return the gitHash
	 */
	public static String getVersionGitHash() {
		return versionGitHash;
	}

	/**
	 * @param gitHash
	 *            the gitHash to set
	 */
	public static void setVersionGitHash(String gitHash) {
		versionGitHash = gitHash;
	}

	/**
	 * @param app the app to set
	 */
	public static void setApp(App app) {
		GlobalObjects.app = app;
	}

	/**
	 * @return the app
	 */
	public static App getApp() {
		return app;
	}

	/**
	 * @return the sharingMessage
	 */
	public static String getSharingMessage() {
		return sharingMessage;
	}

	/**
	 * @return the whatToShareTitle
	 */
	public static String getWhatToShareTitle() {
		return whatToShareTitle;
	}

	/**
	 * @return the whatToShare
	 */
	public static String getWhatToShare() {
		return whatToShare;
	}

	/**
	 * @return the analyticsEnabled
	 */
	public static boolean isAnalyticsEnabled() {
		return analyticsEnabled;
	}

}

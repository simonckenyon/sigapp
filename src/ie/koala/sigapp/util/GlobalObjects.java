/**
 * 
 */
package ie.koala.sigapp.util;

import ie.koala.sigapp.xml.App;

/**
 * @author simon
 *
 */
public class GlobalObjects {
	public static App app = null;
	
	private static String versionName = "";
	private static String versionCode = "";
	private static String versionBuildTimestamp = "";
	private static String versionGitHash = "";
	
	/**
	 * @return the versionName
	 */
	public static String getVersionName() {
		return versionName;
	}
	
	/**
	 * @param versionName the versionName to set
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
	 * @param versionCode the versionCode to set
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
	 * @param buildTimestamp the buildTimestamp to set
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
	 * @param gitHash the gitHash to set
	 */
	public static void setVersionGitHash(String gitHash) {
		versionGitHash = gitHash;
	}

}

/**
 * 
 */
package ie.koala.sigapp.web;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import ie.koala.sigapp.util.GlobalObjects;

/**
 * Wiki tag for the <a href="http://code.google.com/apis/info/">Google Chart
 * API</a>
 * 
 */
public class InfoTag extends HTMLTag implements INoBodyParsingTag {
	
	private final static String TAG = InfoTag.class.getSimpleName();
	
	final static public HashSet<String> ALLOWED_ATTRIBUTES_SET = new HashSet<String>();

	final static public String[] ALLOWED_ATTRIBUTES = { "timestamp", "name",
			"code", "githash" };

	static String appVersionName;
	static String appVersionCode;
	static String appVersionBuildTimestamp;
	static String appVersionGitHash;

	static {
		for (int i = 0; i < ALLOWED_ATTRIBUTES.length; i++) {
			ALLOWED_ATTRIBUTES_SET.add(ALLOWED_ATTRIBUTES[i]);
		}

		appVersionName = GlobalObjects.getVersionName();
		appVersionCode = GlobalObjects.getVersionCode();
		appVersionBuildTimestamp = GlobalObjects.getVersionBuildTimestamp();
		appVersionGitHash = GlobalObjects.getVersionGitHash();

	}

	public InfoTag() {
		super("info");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();
		Set<String> keysSet = tagAtttributes.keySet();
		buf.append("<p>");
		for (String str : keysSet) {
			Log.d(TAG, "str=" + str);
			if (str.equals("timestamp")) {
				buf.append(appVersionBuildTimestamp);
			} else if (str.equals("name")) {
				buf.append(appVersionName);
			} if (str.equals("code")) {
				buf.append(appVersionCode);
			} if (str.equals("githash")) {
				buf.append(appVersionGitHash);
			}
		}
		buf.append("</p>");
	}

	@Override
	public boolean isAllowedAttribute(String attName) {
		return ALLOWED_ATTRIBUTES_SET.contains(attName);
	}

}
/**
 * 
 */
package ie.koala.sigapp.wiki;

import info.bliki.wiki.model.WikiModel;

/**
 * @author simon
 * 
 */
public class AppWiki extends WikiModel {
	public AppWiki(String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
	}

	public void parseInternalImageLink(String imageNamespace,
			String rawImageLink) {
		
	}
}
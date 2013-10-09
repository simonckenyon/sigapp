package ie.koala.sigapp.web;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;

public class AppWikiModel extends WikiModel {

	static {
		Configuration.DEFAULT_CONFIGURATION.addTokenTag("info", new InfoTag());
	}

	public AppWikiModel() {
		super("assets://app/images/${image}", "assets://app/web/${title}.wiki");
	}

}

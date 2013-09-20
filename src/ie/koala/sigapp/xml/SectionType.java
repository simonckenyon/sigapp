package ie.koala.sigapp.xml;

public enum SectionType {

	UNKNOWN			("unknown"),
	WIKI			("wiki"),
	IMAGE_GALLERY	("image_gallery"),
	VIDEO			("video");

	SectionType(String id) {
		this.id = id;
	}
	
	final String id;
	
	public static SectionType find(String type) {
		for (SectionType st: SectionType.values()) {
			if (st.id.equals(type)) {
				return st;
			}
		}
		return null;
	}

	public String formatted() {
		switch (this) {
			default:
			case UNKNOWN:
				return "unknown";
			case WIKI:
				return "wiki";
			case IMAGE_GALLERY:
				return "image gallery";
			case VIDEO:
				return "video";
		}
	}
}

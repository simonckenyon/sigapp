package ie.koala.sigapp.xml;

import java.util.ArrayList;
import java.util.List;

public class App {
	String name;
	Theme theme;
	List<Section> sections = new ArrayList<Section>();

	/**
	 * @return all the sections
	 */
	public List<Section> getAllSections() {
		return sections;
	}

	/**
	 * 
	 * @return the number of sections in the app
	 */
	public int sectionCount() {
		return sections.size();
	}
	
	/**
	 * @param add a section to the list
	 */
	public void addSection(Section section) {
		sections.add(section);
	}

	/**
	 * 
	 * @return the section at the specified location
	 */
	public Section getSectionAt(int location) {
		return sections.get(location);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
}

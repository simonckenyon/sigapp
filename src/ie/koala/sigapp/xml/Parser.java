/**
 * 
 */
package ie.koala.sigapp.xml;

import ie.koala.sigapp.util.GlobalObjects;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * @author simon
 *
 */
public class Parser extends DefaultHandler {
	
	private final static String TAG = Parser.class.getSimpleName();

	private String s;
	
	private App app;
	private Theme theme;
	private Section section;

	public void parseDocument(InputStream xmlFile) {
		Log.d(TAG, "in parseDocument(" + xmlFile.toString() + ")");

		// get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			// get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			// parse the file and also register this class for call backs
			sp.parse(xmlFile, this);
		}
		catch (SAXException se) {
			se.printStackTrace();
		}
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	// Event Handlers
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		s = "";
		if (qName.equalsIgnoreCase("app")) {
			Log.d(TAG, "found start of app");
			app = new App();
			app.setName(attributes.getValue("name"));
		} else if (qName.equalsIgnoreCase("theme")) {
			Log.d(TAG, "found start of theme");
			theme = new Theme();
			theme.setName(attributes.getValue("name"));
			theme.setColour(attributes.getValue("colour"));
		} else if (qName.equalsIgnoreCase("section")) {
			Log.d(TAG, "found start of section");
			section = new Section();
			section.setName(attributes.getValue("name"));
			section.setTitle(attributes.getValue("title"));
			section.setType(attributes.getValue("type"));
		} else if (qName.equalsIgnoreCase("url")) {
			Log.d(TAG, "found start of url");
		} else if (qName.equalsIgnoreCase("image")) {
			Log.d(TAG, "found start of image");
		} else if (qName.equalsIgnoreCase("video")) {
			Log.d(TAG, "found start of video");
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		s = new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("app")) {
			GlobalObjects.setApp(app);
		} else if (qName.equalsIgnoreCase("theme")) {
			// add it to the list
			app.setTheme(theme);
			theme = null;
		} else if (qName.equalsIgnoreCase("section")) {
			app.addSection(section);
			section = null;
		} else if (qName.equalsIgnoreCase("url")) {
			section.setUrl(s);
			Log.d(TAG, "url=\"" + s + "\"");
		} else if (qName.equalsIgnoreCase("image")) {
			section.setImage(s);
			Log.d(TAG, "image=\"" + s + "\"");
		} else if (qName.equalsIgnoreCase("video")) {
			section.setVideo(s);
			Log.d(TAG, "video=\"" + s + "\"");
		}
	}
}
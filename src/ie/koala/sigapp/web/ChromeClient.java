package ie.koala.sigapp.web;

import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class ChromeClient extends WebChromeClient {
	
	Activity activity;

	public ChromeClient(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void onProgressChanged(WebView view, int progress) {
		// Activities and WebViews measure progress with different scales.
		// The progress meter will automatically disappear when we reach 100%
		activity.setProgress(progress * 1000);
	}


}

package ie.koala.sigapp.web;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewClient extends WebViewClient {
	
	private final static String TAG = ViewClient.class.getSimpleName();
	
	Activity activity;

	public ViewClient(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		Toast.makeText(activity, "Oh no! " + description,
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Log.d(TAG, "shouldOverrideUrlLoading(): url=" + url);
		if (url.startsWith("assets://")) {
			return false;
		} else {
			// Otherwise, the link is not for a page on my site, so
			// launch another Activity that handles URLs
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri
					.parse(url));
			activity.startActivity(intent);
			return true;
		}
	}

	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		Log.d(TAG, "shouldInterceptRequest(): url=" + url);
		AssetManager am = activity.getResources().getAssets();
		return ResourceResponse.response(url, am);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		Log.d(TAG, "onLoadResource(): url=" + url);
	}

}
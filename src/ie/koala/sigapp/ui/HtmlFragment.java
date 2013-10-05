package ie.koala.sigapp.ui;

import ie.koala.sigapp.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import ie.koala.sigapp.util.ResourceResponse;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link HtmlFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link HtmlFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class HtmlFragment extends Fragment {

	private final static String TAG = HtmlFragment.class.getSimpleName();

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private OnFragmentInteractionListener mListener;

	WebView html;

	int position;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param position
	 *            Tab position of this html instance.
	 * 
	 * @return A new instance of fragment HtmlFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static HtmlFragment newInstance(int position) {
		HtmlFragment fragment = new HtmlFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);
		return fragment;
	}

	public HtmlFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = (int) getArguments().getLong(ARG_SECTION_NUMBER);
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		final Activity activity;

		activity = getActivity();
		// activity.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_html, container, false);
		html = (WebView) view.findViewById(R.id.html);
		WebSettings webSettings = html.getSettings();
		webSettings.setJavaScriptEnabled(true);
		html.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		html.setWebViewClient(new WebViewClient() {
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
					startActivity(intent);
					return true;
				}
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				Log.d(TAG, "shouldInterceptRequest(): url=" + url);
				AssetManager am = getResources().getAssets();

				return ResourceResponse.response(url, am);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				Log.d(TAG, "onLoadResource(): url=" + url);
			}
		});
		try {
			String url = GlobalObjects.app.getSectionAt(position).getUrl();
			Log.d(TAG, "onCreateView(): url=" + url);
			html.loadUrl("assets://" + url);
		} catch (Exception e) {
			html.loadData("Error: can't show help. [Exception]", "text/html",
					"");
		}
		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public boolean canGoBack() {
		Log.d(TAG, "HtmlFragment.canGoBack() called");
		return html.canGoBack();
	}

	public void goBack() {
		html.goBack();
	}
}

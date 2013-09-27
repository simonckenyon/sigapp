package ie.koala.sigapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import ie.koala.sigapp.R;
import ie.koala.sigapp.util.GlobalObjects;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link WikiFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link WikiFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class WikiFragment extends Fragment {

	private final static String TAG = WikiFragment.class.getSimpleName();

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private OnFragmentInteractionListener mListener;

	WebView wiki;

	int position;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param position
	 *            Tab position of this wiki instance.
	 * 
	 * @return A new instance of fragment WikiFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static WikiFragment newInstance(int position) {
		WikiFragment fragment = new WikiFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);
		return fragment;
	}

	public WikiFragment() {
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
		view = inflater.inflate(R.layout.fragment_wiki, container, false);
		wiki = (WebView) view.findViewById(R.id.wiki);
		WebSettings webSettings = wiki.getSettings();
		webSettings.setJavaScriptEnabled(true);
		wiki.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});
		wiki.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d(TAG, "shouldOverrideUrlLoading(): url=" + url);
				return false;
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				Log.d(TAG, "shouldInterceptRequest(): url=" + url);
				
				// remove "assets://" from beginning of url
				String fileName = url.substring(9);
				String mimeType;
				if (fileName.endsWith("png")) {
					mimeType = "image/png";
				} else if (fileName.endsWith("jpg")) {
					mimeType = "image/jpeg";
				} else if (fileName.endsWith("html")) {
					mimeType = "text/html";
				} else if (fileName.endsWith("js")) {
					mimeType = "text/javascript";
				} else if (fileName.endsWith("css")) {
					mimeType = "text/css";
				} else {
					mimeType = "";
				}
				Log.d(TAG, "shouldInterceptRequest(): fileName=" + fileName);
				AssetManager am = getResources().getAssets();
				InputStream in_s;
				try {
					in_s = am.open(fileName);
					String encoding = "UTF-8";
					WebResourceResponse response = new WebResourceResponse(
							mimeType, encoding, in_s);
					return response;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				Log.d(TAG, "onLoadResource(): url=" + url);
			}
		});
		try {
			String url = GlobalObjects.app.getSectionAt(position).getUrl();
			Log.d(TAG, "onCreateView(): url=" + url);
			wiki.loadUrl("assets://" + url);
		} catch (Exception e) {
			wiki.loadData("Error: can't show help. [Exception]", "text/html",
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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}

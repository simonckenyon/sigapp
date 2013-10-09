package ie.koala.sigapp.ui;

import com.actionbarsherlock.app.SherlockFragment;

import ie.koala.sigapp.skynetlabs.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import ie.koala.sigapp.web.ChromeClient;
import ie.koala.sigapp.web.ViewClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link WebFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link WebFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class WebFragment extends SherlockFragment {

	private final static String TAG = WebFragment.class.getSimpleName();

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
	public static WebFragment newInstance(int position) {
		WebFragment fragment = new WebFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);
		return fragment;
	}

	public WebFragment() {
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
		String url = GlobalObjects.app.getSectionAt(position).getUrl();
		String fullUrl = "assets://" + url;
		Log.d(TAG, "onCreateView(): fullUrl=" + fullUrl);

		// activity.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_html, container, false);
		html = (WebView) view.findViewById(R.id.html);
		WebSettings webSettings = html.getSettings();
		webSettings.setJavaScriptEnabled(true);
		html.setWebChromeClient(new ChromeClient(activity));
		html.setWebViewClient(new ViewClient(activity));
		try {
			html.loadUrl(fullUrl);
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

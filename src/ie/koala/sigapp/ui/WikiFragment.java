package ie.koala.sigapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import ie.koala.sigapp.R;
import ie.koala.sigapp.R.layout;
import ie.koala.sigapp.util.GlobalObjects;
import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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
	 * The fragment argument representing the section number for this
	 * fragment.
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
			position = (int)getArguments().getLong(ARG_SECTION_NUMBER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_wiki, container, false);
		wiki = (WebView) view.findViewById(R.id.wiki);
		try {
			AssetManager am = getResources().getAssets();
			String url = GlobalObjects.app.getSectionAt(position).getUrl();
			Log.d(TAG, "url=" + url);
			InputStream in_s = am.open(url);
			Writer writer = new StringWriter();
			char[] buffer = new char[2048];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(in_s,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				in_s.close();
			}
			wiki.loadData(writer.toString(), "text/html", "");
		} catch (IOException e) {
			wiki.loadData("Error: can't show help. [IOException]",
					"text/html", "");
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

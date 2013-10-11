package ie.koala.sigapp.ui;

import java.io.File;
import com.actionbarsherlock.app.SherlockFragment;

import ie.koala.sigapp.simonkenyon.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link VideoFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class VideoFragment extends SherlockFragment {

	private final static String TAG = VideoFragment.class.getSimpleName();
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private OnFragmentInteractionListener mListener;

	VideoView video;

	int position;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param position
	 *            Tab position of this gallery instance.
	 * 
	 * @return A new instance of fragment ImageFragment.
	 */
	public static VideoFragment newInstance(int position) {
		VideoFragment fragment = new VideoFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);
		return fragment;
	}

	public VideoFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			position = (int) getArguments().getLong(ARG_SECTION_NUMBER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		final Activity activity;

		activity = getActivity();

		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_video, container, false);
		video = (VideoView) view.findViewById(R.id.video);
		String videoFile = null;
		try {
			videoFile = GlobalObjects.getApp().getSectionAt(position).getVideo();
			Log.d(TAG, "onCreateView(): videoFile=" + videoFile);
			playVideo(activity, videoFile);
		} catch (Exception e) {
			Log.d(TAG, "Exception: can't load video (" + videoFile + ")");
		}
		return view;
	}

	private void playVideo(Activity activity, String fileName) {
		activity.setContentView(video);
		Uri videoUrl = Uri.fromFile(new File(fileName));
		video.setVideoURI(videoUrl);
		video.setMediaController(new MediaController(activity));
		video.requestFocus();
		video.start();
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

}

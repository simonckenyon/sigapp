package ie.koala.sigapp.ui;

import java.io.IOException;
import java.io.InputStream;

import com.actionbarsherlock.app.SherlockFragment;

import ie.koala.sigapp.simonkenyon.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ImageFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class ImageFragment extends SherlockFragment {
	
	private final static String TAG = ImageFragment.class.getSimpleName();
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private OnFragmentInteractionListener mListener;

	ImageView image;

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
	public static ImageFragment newInstance(int position) {
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_SECTION_NUMBER, position);
		fragment.setArguments(args);
		return fragment;
	}

	public ImageFragment() {
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
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_image, container, false);
		image = (ImageView) view.findViewById(R.id.image);
		String img = null;
		try {
			img = GlobalObjects.app.getSectionAt(position).getImage();
			Log.d(TAG, "onCreateView(): img=" + img);
			AssetManager am = getResources().getAssets();
			InputStream in_s;
			in_s = am.open(img);
			Bitmap bm = BitmapFactory.decodeStream(in_s);
			image.setImageBitmap(bm);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.d(TAG, "Exception: can't load image (" + img + ")");
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

}

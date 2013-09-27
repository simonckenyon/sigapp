package ie.koala.sigapp.ui;

import ie.koala.sigapp.R;
import ie.koala.sigapp.ui.WikiFragment.OnFragmentInteractionListener;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.xml.Parser;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class AppMainActivity extends FragmentActivity implements
		OnFragmentInteractionListener {

	private final static String TAG = AppMainActivity.class.getSimpleName();

	public static final int ACTIVITY_LOGIN = 0;
	public static final int ACTIVITY_MAIN = 1;
	public static final int ACTIVITY_SETTINGS = 2;
	public static final int ACTIVITY_ABOUT = 3;
	public static final int ACTIVITY_HELP = 4;
	public static final int ACTIVITY_FEEDBACK = 5;
	public static final int ACTIVITY_PURCHASE = 6;
	public static final int ACTIVITY_SHEET_PREVIEW = 7;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			AssetManager am = getResources().getAssets();
			InputStream in_s = am.open("app/app.xml");
			Parser parser = new Parser();
			parser.parseDocument(in_s);
			Log.d(TAG, "xml parsed");
		} catch (IOException e) {
			e.printStackTrace();
		}

		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		int itemId = item.getItemId();
		if (itemId == R.id.action_settings) {
			intent = new Intent(AppMainActivity.this, SettingsActivity.class);
			startActivityForResult(intent, ACTIVITY_SETTINGS);
			return true;
		}
		return false;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			// getItem is called to instantiate the fragment for the given page.
			Log.d(TAG, "position=" + position);
			return WikiFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return GlobalObjects.app.sectionCount();
		}

		@Override
		public CharSequence getPageTitle(int location) {
			if (location < getCount()) {
				return GlobalObjects.app.getSectionAt(location).getTitle();
			} else {
				return null;
			}
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
	}

}

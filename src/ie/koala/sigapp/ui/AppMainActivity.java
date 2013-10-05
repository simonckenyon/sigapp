package ie.koala.sigapp.ui;

import ie.koala.sigapp.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import ie.koala.sigapp.xml.Parser;
import ie.koala.sigapp.xml.SectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

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

	SectionsPagerAdapter adapter;
	
	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	private Drawable oldBackground = null;
//	private int currentColor = 0xFF666666;
	private int currentColor = 0xFF96AA39;

	int mPosition;
	List<Fragment> mFragmentList = new ArrayList<Fragment>();

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

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new SectionsPagerAdapter(getSupportFragmentManager());
		
		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		
		tabs.setViewPager(pager);

		changeColor(currentColor);
		
		tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				Log.d(TAG, "page selected=" + pos);
				mPosition = pos;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

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
			SectionType sectionType = GlobalObjects.app.getSectionAt(position)
					.getType();
			Fragment f = null;

			// getItem is called to instantiate the fragment for the given page.
			Log.d(TAG, "getItem(): position=" + position + " type="
					+ sectionType);
			switch (sectionType) {
			case HTML:
				Log.d(TAG, "html fragment");
				f = HtmlFragment.newInstance(position);
				break;
			case IMAGE:
				Log.d(TAG, "image fragment");
				f = ImageFragment.newInstance(position);
				break;
			case WIKI:
				Log.d(TAG, "wiki fragment");
				f = WikiFragment.newInstance(position);
				break;
			case VIDEO:
				Log.d(TAG, "video fragment");
				f = VideoFragment.newInstance(position);
				break;
			default:
				Log.d(TAG, "unknown fragment");
				break;
			}
			mFragmentList.add(position, f);
			return f;
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
	public void onBackPressed() {
		Log.d(TAG, "back selected");
		SectionType sectionType = GlobalObjects.app.getSectionAt(mPosition)
				.getType();
		boolean goback;
		switch (sectionType) {
		case HTML:
			Log.d(TAG, "html page");
			HtmlFragment html = ((HtmlFragment) mFragmentList.get(mPosition));
			goback = html.canGoBack();
			if (!goback) {
				Log.d(TAG, "cannot go back, so pass to super");
				this.finish();
//				super.onBackPressed();
			} else {
				Log.d(TAG, "can go back");
				html.goBack();
			}
			break;
		case WIKI:
			Log.d(TAG, "html page");
			WikiFragment wiki = ((WikiFragment) mFragmentList.get(mPosition));
			goback = wiki.canGoBack();
			if (!goback) {
				Log.d(TAG, "cannot go back, so pass to super");
				this.finish();
//				super.onBackPressed();
			} else {
				Log.d(TAG, "can go back");
				wiki.goBack();
				
			}
			break;
		case IMAGE:
		case VIDEO:
		default:
			Log.d(TAG, "misc page");
			this.finish();
			break;
		}
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO Auto-generated method stub
	}
	
	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}

		currentColor = newColor;

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};


}

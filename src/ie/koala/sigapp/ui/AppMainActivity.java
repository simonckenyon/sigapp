package ie.koala.sigapp.ui;

import ie.koala.sigapp.simonkenyon.R;
import ie.koala.sigapp.util.GlobalObjects;
import ie.koala.sigapp.util.OnFragmentInteractionListener;
import ie.koala.sigapp.xml.Parser;
import ie.koala.sigapp.xml.Section;
import ie.koala.sigapp.xml.SectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

//import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class AppMainActivity extends SherlockFragmentActivity implements
		OnFragmentInteractionListener {

	private final static String TAG = AppMainActivity.class.getSimpleName();

	public static final int ACTIVITY_SETTINGS = 1;
	public static final int ACTIVITY_QR_CODE = 2;

	private ShareActionProvider mShareActionProvider;

	SectionsPagerAdapter adapter;

	private final Handler handler = new Handler();

	// private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	private Drawable oldBackground = null;
	private int currentColour = 0xff5a5a5a;

	int mPosition;
	List<SherlockFragment> mFragmentList = new ArrayList<SherlockFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get info from the manifest
		getInfo();

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);

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

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new SectionsPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		currentColour = GlobalObjects.app.getTheme().getIntColour();
		changeColor(currentColour);

		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// When the tab is selected, switch to the
				// corresponding page in the ViewPager.
				mPosition = tab.getPosition();
				pager.setCurrentItem(mPosition);
			}

			public void onTabUnselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab,
					FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between pages, select the
				// corresponding tab.
				mPosition = position;
				getActionBar().setSelectedNavigationItem(position);
			}
		});

		setupTabs(actionBar, tabListener);

	}

	private void getInfo() {

		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			GlobalObjects.setVersionName(String.format(
					getString(R.string.app_version_name_format),
					info.versionName));
			GlobalObjects.setVersionCode(String.format(
					getString(R.string.app_version_code_format),
					Integer.toString(info.versionCode)));
			GlobalObjects.setVersionBuildTimestamp(String.format(
					getString(R.string.app_version_build_timestamp_format),
					getString(R.string.app_version_build_timestamp)));
			GlobalObjects.setVersionGitHash(String.format(
					getString(R.string.app_version_git_hash_format),
					getString(R.string.app_version_git_hash).substring(0, 10)));
			Log.i(TAG, "versionName=" + GlobalObjects.getVersionName());
			Log.i(TAG, "versionCode=" + GlobalObjects.getVersionCode());
			Log.i(TAG,
					"versionBuildTimestamp="
							+ GlobalObjects.getVersionBuildTimestamp());
			Log.i(TAG, "versionGitHash=" + GlobalObjects.getVersionGitHash());
		} catch (Exception e) {
			Log.e(TAG, "Error getting version");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);

		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();
		setShareIntent(getDefaultIntent());

		// Return true to display menu
		return true;
	}

	// Call to update the share intent
	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

	/**
	 * Defines a default (dummy) share intent to initialize the action provider.
	 * However, as soon as the actual content to be used in the intent is known
	 * or changes, you must update the share intent by again calling
	 * mShareActionProvider.setShareIntent()
	 */
	private Intent getDefaultIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_TEXT,
				getResources().getString(R.string.share_url));
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getResources().getString(R.string.share_subject));
		intent.setType("text/plain");
		return intent;
	}

	private void setupTabs(ActionBar actionBar,
			ActionBar.TabListener tabListener) {
		// Add tabs, specifying the tab's text and TabListener
		List<Section> sections = GlobalObjects.app.getAllSections();
		int count = sections.size();

		for (int i = 0; i < count; i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(sections.get(i).getTitle())
					.setTabListener(tabListener));
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		int itemId = item.getItemId();
		if (itemId == R.id.action_qr_code) {
			intent = new Intent(AppMainActivity.this, QrCodeActivity.class);
			startActivityForResult(intent, ACTIVITY_QR_CODE);
			return true;
			// } else if (itemId == R.id.action_settings) {
			// intent = new Intent(AppMainActivity.this,
			// SettingsActivity.class);
			// startActivityForResult(intent, ACTIVITY_SETTINGS);
			// return true;
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
		public SherlockFragment getItem(int position) {
			SectionType sectionType = GlobalObjects.app.getSectionAt(position)
					.getType();
			SherlockFragment f = null;

			// getItem is called to instantiate the fragment for the given page.
			Log.d(TAG, "getItem(): position=" + position + " type="
					+ sectionType);
			switch (sectionType) {
			case WEB:
				Log.d(TAG, "web fragment");
				f = WebFragment.newInstance(position);
				break;
			case IMAGE:
				Log.d(TAG, "image fragment");
				f = ImageFragment.newInstance(position);
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
		case WEB:
			Log.d(TAG, "web page");
			WebFragment html = ((WebFragment) mFragmentList.get(mPosition));
			goback = html.canGoBack();
			if (!goback) {
				Log.d(TAG, "cannot go back, so pass to super");
				this.finish();
				// super.onBackPressed();
			} else {
				Log.d(TAG, "can go back");
				html.goBack();
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

	private void changeColor(int newColour) {

		// tabs.setIndicatorColor(newColour);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColour);
			Drawable bottomDrawable = getResources().getDrawable(
					R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] {
					colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						oldBackground, ld });

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

		currentColour = newColour;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColour);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColour = savedInstanceState.getInt("currentColor");
		changeColor(currentColour);
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

	void setPage(int position) {
		pager.setCurrentItem(position);
	}
}
